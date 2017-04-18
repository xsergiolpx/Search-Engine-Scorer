#!/bin/bash

if [ "$1" = "--cran" ]
then
	dir=../Cranfield_DATASET/cran
	directory="collection-cran"
	truth="../Cranfield_DATASET/cran_all_queries.tsv"
elif [ "$1" = "--time" ]
then
	dir=../Time_DATASET/time
        directory="collection-time"
        truth="../Time_DATASET/time_all_queries.tsv"
else 
	echo "Use --cran or --time"
fi

if [ "$1" = "--cran" ] || [ "$1" = "--time" ]
then

# Folder with html files

# change the working directory
cd $directory

# Set the collection
echo
echo "Creating colletions..."
find $dir -iname \*.html | java it.unimi.di.big.mg4j.document.FileSetDocumentCollection -f HtmlDocumentFactory -p encoding=UTF-8 default.collection > /dev/null &
find $dir -iname \*.html | java it.unimi.di.big.mg4j.document.FileSetDocumentCollection -f HtmlDocumentFactory -p encoding=UTF-8 english.collection > /dev/null &
find $dir -iname \*.html | java it.unimi.di.big.mg4j.document.FileSetDocumentCollection -f HtmlDocumentFactory -p encoding=UTF-8 stopwords.collection > /dev/null &

wait

# Create inverted index for title with default stemmer
echo
echo "Inverted index with default stemmer..."
java it.unimi.di.big.mg4j.tool.IndexBuilder --downcase -S default.collection default > /dev/null &

# Same with English Stemmer
echo
echo "Inverted index with English stemmer..."
java it.unimi.di.big.mg4j.tool.IndexBuilder -t it.unimi.di.big.mg4j.index.snowball.EnglishStemmer -S english.collection english > /dev/null &

# Same with stopwords. If it doesn't work run it from the command line
echo
echo "Inverted index with English Stopwords stemmer..."
java it.unimi.di.big.mg4j.tool.IndexBuilder -t homework.EnglishStemmerStopwords -S stopwords.collection stopwords  > /dev/null &

wait

cd ..

echo
echo "Done!!"

fi
