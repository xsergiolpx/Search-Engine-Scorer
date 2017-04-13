#!/bin/bash

# Folder with html files
dir=Cranfield_DATASET/cran

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

echo
echo "Done!!"


