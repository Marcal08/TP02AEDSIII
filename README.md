# AEDsIII - TP02
## Rotinas Implementadas
- Implementação de um algoritmo de busca de livros a partir de uma lista invertida;
- Implementação de rotina de normalização de texto (letras minúsculas e sem acento);
- Ajuste dos códigos de criação, atualização e exclusão na classe ArquivoLivros para flexibilidade com a lista flexível e controle das stopWords:
    - HashSet STOPWORDS: Conjunto de dados do tipo Hash que armazena diversas stopwords.
    - create(): Método responsável por adicionar um novo livro à base de dados e atualizar a lista invertida com os termos presentes no novo registro.
    - update(): Função que atualiza as informações de um livro já existente na base de dados, fazendo as modificações necessárias na lista invertida de acordo com as alterações realizadas.
    - delete(): Método que remove um livro da base de dados, eliminando simultaneamente todas as ocorrências dos termos associados a este livro na lista invertida.

## Relato do Grupo
- Todos os requisitos do projeto foram efetivamente implementados. A fase mais desafiadora foi a inicial, na qual precisávamos compreender as alterações necessárias para a implementação da lista invertida. Uma complicação adicional surgiu com a necessidade de garantir que os IDs não se repetissem, o que exigiu uma atenção extra. Embora a implementação em si tenha transcorrido de maneira tranquila, se houvesse que destacar um aspecto particularmente complexo, seria a parte da atualização. Além disso, o desenvolvimento do projeto provou ser uma experiência tanto divertida quanto educativa. O grupo conseguiu perceber a relevância e o funcionamento de uma lista invertida, enriquecendo assim nosso conhecimento no campo.
## Questionário
- A inclusão de um livro acrescenta os termos do seu título à lista invertida? **
- A alteração de um livro modifica a lista invertida removendo ou acrescentando termos do título?
- A remoção de um livro gera a remoção dos termos do seu título na lista invertida?
- Há uma busca por palavras que retorna os livros que possuam essas palavras? **Sim**
- Essa busca pode ser feita com mais de uma palavra? **Sim**
- As stop words foram removidas de todo o processo?
- Que modificação, se alguma, você fez para além dos requisitos mínimos desta tarefa? **Tratamento de palavras repetidas nos títulos**
- O trabalho está funcionando corretamente? **Sim**
- O trabalho está completo?
- O trabalho é original e não a cópia de um trabalho de um colega? **Sim**
## Autores
- [@fabioacandrade](https://www.github.com/fabioacandrade)
- [@LucasAlkmimBarros](https://www.github.com/LucasAlkmimBarros)
- [@Marcal08](https://www.github.com/Marcal08)
- [@PRMaia](https://www.github.com/PRMaia)
