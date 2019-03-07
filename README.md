[![codecov](https://codecov.io/gh/genome-nexus/genome-nexus/branch/master/graph/badge.svg)](https://codecov.io/gh/genome-nexus/genome-nexus)
[![codebeat badge](https://codebeat.co/badges/d599b538-43e3-4828-8f27-820031393196)](https://codebeat.co/projects/github-com-genome-nexus-genome-nexus-master)

[![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy)

| branch | master | rc |
| --- | --- | --- |
| status | [![Build Status](https://travis-ci.org/genome-nexus/genome-nexus.svg?branch=master)](https://travis-ci.org/genome-nexus/genome-nexus/branches) | [![Build Status](https://travis-ci.org/genome-nexus/genome-nexus.svg?branch=rc)](https://travis-ci.org/genome-nexus/genome-nexus/branches) |

# Genome Nexus
Genome Nexus, a comprehensive one-stop resource for fast, automated and
high-throughput annotation and interpretation of genetic variants in cancer.
Genome Nexus will integrate information from a variety of existing resources,
including databases that convert DNA changes to protein changes, predict the
functional effects of protein mutations, and contain information about mutation
frequencies, gene function, variant effects, and clinical actionability.

Three goals:

1. Data collection from various annotation sources
2. Integration of heterogeneous information into a harmonized structure and
programmatic interface
3. Dissemination of the diverse information in a hierarchical digestible way
for interpreting variants and patients.

## Run

### Alternative 1 - run genome-nexus, mongoDB and genome-nexus-vep in docker containers
First, set environment variables for Ensembl Release, VEP Assembly and location of VEP Cache. If these are not, the default values from `.env` will be set.

The reference genome and Ensembl release must be consistent with a version in [genome-nexus-importer/data/](https://github.com/genome-nexus/genome-nexus-importer/tree/rc/data).
For example `grch37_ensembl92`, `grch38_ensembl92` or `grch38_ensembl95`:
```
export REF_ENSEMBL_VERSION=grch38_ensembl92
```

If you would like to do local VEP annotations instead of using the public Ensembl API,
please add `gn_vep.region.url=http://vep:8080/vep/human/region/VARIANT` in your `application.properties`.
This will require you to download the VEP cache files for the preferred Ensembl Release and Reference genome,
see our documentation on [downloading the Genome Nexus VEP Cache](https://github.com/genome-nexus/genome-nexus-vep/blob/master/README.md#create-vep-cache).
This will take several hours.
```
# Set local cache dir
export VEP_CACHE=<local_vep_cache>

# GRCh38 or GRCh37
export VEP_ASSEMBLY=GRCh38
```

Run docker-compose to create images and containers:
```
docker-compose up --build -d
```

Run without recreating images:
```
docker-compose up -d
```

Run without Genome Nexus VEP:
```
# Start both the Web and DB (dependency of Web) containers
docker-compose up -d web
```

Stop and remove containers:
```
docker-compose down
```

### Alternative 2 - run genome-nexus locally, but mongoDB in docker container
```
# the genomenexus/gn-mongo images comes with all the required tables imported
# change latest to different version if necessary (only need to run this once)
docker run --name=gn-mongo --restart=always -p 27017:27017 -d genomenexus/gn-mongo:latest 
mvn  -DskipTests clean install
java -jar web/target/web-*.war
```

### Alternative 3 - install mongoDB locally and run with local java
Install mongoDB manually. Then follow instructions in
[genome-nexus-importer](https://github.com/genome-nexus/genome-nexus-importer)
to initialize the database.

After that run this:
```
mvn clean install
java -jar web/target/web-*.war
```

## Update data
If you need to update the data files see [genome-nexus-importer](https://github.com/genome-nexus/genome-nexus-importer)

## Programmatic access through R/Python
See [notebooks/](notebooks/)
