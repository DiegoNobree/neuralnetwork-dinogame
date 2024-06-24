# neuroevolution-dino
Usando redes neurais artificiais e algoritmo genético para treinar bot para jogar o jogo Chrome Dino.


## Arquitetura da Rede Neural

Cada dinossauro na população possui 2 redes neurais:

 * Rede neural responsável por evitar cactos - 5 entradas, 8 neurônios na camada oculta e 2 neurônios de saída. As entradas para esta rede neural são:
    * Distância horizontal até o próximo obstáculo
    * Altura do próximo obstáculo
    * Largura do próximo obstáculo
    * Posição y do dinossauro
    * Velocidade do jogo

   A rede tem 2 saídas - o dinossauro pulará se a primeira saída for maior que a segunda.
    
 * Rede neural responsável por evitar os pássaros - 6 entradas, 10 neurônios na camada oculta e 3 neurônios de saída. As entradas para esta rede neural são:
    * Distância horizontal até o próximo obstáculo
    * Altura do próximo obstáculo
    * Largura do próximo obstáculo
    * Posição y do obstáculo
    * Posição y do dinossauro
    * Velocidade do jogo

   A rede tem 3 saídas - pular, abaixar, nada.

## Opções do algoritmo genético

 * Tamanho da população: 1000
 * Elitismo: 0,2
 * Comportamento aleatório: 0,2
 * Taxa de mutação: 0,1
 * Desvio padrão da mutação: 0,5
 * Número de filhos: 1
 
### Codificação da rede neural

Cada rede neural é achatada para um array unidimensional de pesos. Os primeiros elementos do array são os pesos que conectam a camada de entrada com a primeira camada oculta e assim por diante.

### Crossover

O filho inicialmente pega todos os pesos de um de seus pais. Para cada peso da rede do filho, há uma chance de 50% de ser substituído pelo peso correspondente da rede do segundo pai.

### Seleção

O algoritmo de seleção usado é a Seleção por Torneio (O melhor conjunto de pesos, e replicado para os demais)


## Biblioteca gráfica

Java Processing 3.5.4: https://processing.org/

