# circles-hough
Java Hough Circles Transform.
This program calculates the HOUGH TRANSFORM for circular figures and prints all various steps
of the algorithm in .jpeg figures inside the  `results` folder.

It requires: `mvn` and minimum Java version `8`, 


To compile:
```bash
mvn clean
mvn install
```

It will generate the `.jar` file inside the `target` folder.
Run the executable with the following arguments:
* filepath of the source image
* Gaussian Blur Sigma
* Minimum Radius
* Maximum Radius
* Number of circles to draw (not implemented yet) 

For istance:
```
java -jar target/circles-hough.jar samples/simple_circle.jpeg 1.4 10 110 1
```
