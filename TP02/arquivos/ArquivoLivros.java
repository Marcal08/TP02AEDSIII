package arquivos;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import aeds3.Arquivo;
import aeds3.ArvoreBMais;
import aeds3.HashExtensivel;
import aeds3.ListaInvertida;
import aeds3.ParIntInt;
import entidades.Livro;

public class ArquivoLivros extends Arquivo<Livro> {

  HashExtensivel<ParIsbnId> indiceIndiretoISBN;
  ArvoreBMais<ParIntInt> relLivrosDaCategoria;

  public ArquivoLivros() throws Exception {
    super("livros", Livro.class.getConstructor());
    indiceIndiretoISBN = new HashExtensivel<>(
        ParIsbnId.class.getConstructor(),
        4,
        "dados/livros_isbn.hash_d.db",
        "dados/livros_isbn.hash_c.db");
    relLivrosDaCategoria = new ArvoreBMais<>(ParIntInt.class.getConstructor(), 4, "dados/livros_categorias.btree.db");

  }

  private static final Set<String> STOPWORDS = new HashSet<>(Arrays.asList(
      "a", "ao", "aos", "aquela", "aquelas", "aquele", "aqueles", "aquilo", "as", "até", "com", "como", "da", "das",
      "de", "dela", "delas", "dele", "deles", "depois", "do", "dos", "e", "ela", "elas", "ele", "eles", "em", "entre",
      "era", "eram", "essa", "essas", "esse", "esses", "esta", "estamos", "estas", "estava", "estavam", "este",
      "esteja", "estejam", "estejamos", "estes", "estou", "eu", "foi", "fomos", "for", "foram", "fosse", "fossem",
      "haja", "hajam", "hajamos", "hão", "isso", "isto", "já", "lhe", "lhes", "mais", "mas", "me", "mesmo", "meu",
      "meus", "minha", "minhas", "na", "nas", "nem", "no", "nos", "nós", "nossa", "nossas", "nosso", "nossos", "num",
      "numa", "o", "os", "ou", "para", "pela", "pelas", "pelo", "pelos", "por", "qual", "quando", "que", "quem", "se",
      "seja", "sejam", "sejamos", "sem", "só", "sua", "suas", "são", "também", "te", "tem", "tenha", "tenham",
      "tenhamos", "teu", "teus", "tu", "tua", "tuas", "um", "uma", "você", "vocês"));

  public static String semAcento(String str) {
    String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
    Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    return pattern.matcher(nfdNormalizedString).replaceAll("");
  }

  @Override
  public int create(Livro obj) throws Exception {
    ListaInvertida li = new ListaInvertida(4, "dados/dicionario.listainv.db", "dados/blocos.listainv.db");
    int id = super.create(obj);
    String[] palavras = semAcento(obj.getTitulo().toLowerCase()).split(" ");
    for (String palavra : palavras) {
      if (!STOPWORDS.contains(palavra))
        li.create(palavra, id);
    }
    obj.setID(id);
    indiceIndiretoISBN.create(new ParIsbnId(obj.getIsbn(), obj.getID()));
    relLivrosDaCategoria.create(new ParIntInt(obj.getIdCategoria(), obj.getID()));
    return id;
  }

  public Livro readISBN(String isbn) throws Exception {
    ParIsbnId pii = indiceIndiretoISBN.read(ParIsbnId.hashIsbn(isbn));
    if (pii == null)
      return null;
    int id = pii.getId();
    return super.read(id);
  }

  @Override
  public boolean delete(int id) throws Exception {
    ListaInvertida li = new ListaInvertida(4, "dados/dicionario.listainv.db", "dados/blocos.listainv.db");
    Livro obj = super.read(id);
    if (obj != null)
      if (indiceIndiretoISBN.delete(ParIsbnId.hashIsbn(obj.getIsbn()))
          &&
          relLivrosDaCategoria.delete(new ParIntInt(obj.getIdCategoria(), obj.getID()))) {
        String[] palavras = semAcento(obj.getTitulo().toLowerCase()).split(" ");
        for (String palavra : palavras)
          if (!STOPWORDS.contains(palavra))
            li.delete(palavra, id);
        return super.delete(id);
      }
    return false;
  }

  @Override
  public boolean update(Livro novoLivro) throws Exception {
    ListaInvertida li = new ListaInvertida(4, "dados/dicionario.listainv.db", "dados/blocos.listainv.db");
    Livro livroAntigo = super.read(novoLivro.getID());
    if (livroAntigo != null) {

      // Testa alteração do ISBN
      if (livroAntigo.getIsbn().compareTo(novoLivro.getIsbn()) != 0) {
        indiceIndiretoISBN.delete(ParIsbnId.hashIsbn(livroAntigo.getIsbn()));
        indiceIndiretoISBN.create(new ParIsbnId(novoLivro.getIsbn(), novoLivro.getID()));
      }

      // Testa alteração da categoria
      if (livroAntigo.getIdCategoria() != novoLivro.getIdCategoria()) {
        relLivrosDaCategoria.delete(new ParIntInt(livroAntigo.getIdCategoria(), livroAntigo.getID()));
        relLivrosDaCategoria.create(new ParIntInt(novoLivro.getIdCategoria(), novoLivro.getID()));
      }

      // Atualiza o livro
      String[] palavrasAntigas = semAcento(livroAntigo.getTitulo().toLowerCase()).split(" ");
      String[] palavrasNovas = semAcento(novoLivro.getTitulo().toLowerCase()).split(" ");
      for (String palavra : palavrasAntigas)
        if (!STOPWORDS.contains(palavra))
          li.delete(palavra, novoLivro.getID());
      for (String palavra : palavrasNovas)
        if (!STOPWORDS.contains(palavra))
          li.create(palavra, novoLivro.getID());
      return super.update(novoLivro);
    }
    return false;
  }
}
