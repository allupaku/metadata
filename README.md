# Metadata Parser

# Checkout

``` git clone https://github.com/allupaku/metadata.git ```

``` cd metadata ```

# Compiling

this project is built using maven, hence 

```mvn clean install ``` - will create a jar file in target folder.

Also provided two test files test.csv and test.json


# Running

Start the mysql using 
``` docker-compose up -d ```

then you can run the program using

crawl: 

``` java -jar target/simple-metadata-1.0-SNAPSHOT.jar crawl test.csv ```

describe:

``` java -jar target/simple-metadata-1.0-SNAPSHOT.jar describe test.csv ```
