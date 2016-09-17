# Fuzzy c-means image clustering for Java
Java implementation of the Fuzzy c-means clustering algorithm and the image clusterer based on FCM.

## FCM clustering example

Given five Elements and their attributes vectors as follows: **[1,2], [9,1], [2,5], [8,2], [0,0]**.  
Find clusterization into **3 clusters**, **m = 2** (fuzzy partition matrix exponent), 
**epsilon = 0.12** (limit for the maximum difference between corresponding elements of an old and new partition array. 
If limit maximum difference < epsilon, algorithm stops), **maximum number of iterations = 20**.

```java
// prepare the Element array for the Elements
Element[] elements = new Element[5];
// create and assign Elements to the array
elements[0] = Element.setAttributes(new int[] { 1, 2 });
elements[1] = Element.setAttributes(new int[] { 9, 1 });
elements[2] = Element.setAttributes(new int[] { 2, 5 });
elements[3] = Element.setAttributes(new int[] { 8, 2 });
elements[4] = Element.setAttributes(new int[] { 0, 0 });

// create the algorithm object with given parametres
FCM fcm = new FCM(3, 2, 0.12, 20);
// set the input for algorithm
fcm.setInput(elements);
// run the algorith
fcm.runAlgorythm();
// get the result
Element[] result = fcm.getElements();
```

Each element of the result array has now a cluster number assigned. Let's print the result.
```java
for (int i = 0; i < result.length; i++)
	System.out.println("element " + i + " cluster " + result[i].cluster);
```

Console output:
```
element 0 cluster 0
element 1 cluster 1
element 2 cluster 2
element 3 cluster 1
element 4 cluster 0
```
As can be seen elements has been clustered as follows: `{[1,2],[0,0]} , {[9,1],[8,2]} , {[2,5]}`

####Input for the algorithm can also be provided as an int matrix.####

```java
// int[element number][attribute number]
int[][] elements = new int[5][2];
elements[0][0] = 1;
elements[0][1] = 2; 
elements[1][0] = 9; 
elements[1][1] = 1; 
elements[2][0] = 2; 
elements[2][1] = 5; 
elements[3][0] = 8; 
elements[3][1] = 2;
elements[4][0] = 0; 
elements[4][1] = 0; 
```

## FCM image clustering example
There are two ways deliver the input image to the `ImageClusterer`. It can be either `BufferedImage` or `File`.
```java
// create the clusterer object
ImageClusterer clusterer = new ImageClusterer();
// set the image BufferedImage/File
clusterer.setImage(bufferedImage);
// perform clustering with prefered parametres and obtain ClusteredImage as a result
ClusteredImage clusteredImage = clusterer.clusterImage(3, 2, 0.15, 30);
```
`ClusteredImage` is a container object that has the original `BufferedImage`, `Element[]` with clustering 
data and `int` of total clusters. This data can be provided to the `ClusterHighlighter` 
in order to highlight every cluster area into one of the colors.

```java
// pass ClusteredImage to the highlighter
ClusterHighlighter highlighter = new ClusterHighlighterGrayscaleLinear(clusteredImage);
// call highlight() to get the finished BufferedImage
BufferedImage highlighted = highlighter.highlight();
```

There are two more derivative classes to `ClusterHighlighter`: `ClusterHighlighterGrayscaleRandom` 
and `ClusterHighlighterRandomColors`. Use them to get different effects. You can also create your own highlighter. In order to do
that extend `ClusterHighlighter`.

## GUI image clusterer

This simple GUI app uses all of project functionality.
  
![](http://i.imgur.com/4pwYDiH.jpg)

![](http://i.imgur.com/whuyCdM.jpg)

![](http://i.imgur.com/jKa2KI0.jpg)

![](http://i.imgur.com/9KVvPYC.jpg)
