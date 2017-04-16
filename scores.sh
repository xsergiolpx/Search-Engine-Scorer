#!/bin/bash

# Work with cran or time

if [ "$1" = "--cran" ]
then
	directory="collection-cran"
	truth="../Cranfield_DATASET/cran_all_queries.tsv"
elif [ "$1" = "--time" ]
then
        directory="collection-time"
        truth="../Time_DATASET/time_all_queries.tsv"
else 
	echo "Use --cran or --time"
fi

if [ "$1" = "--cran" ] || [ "$1" = "--time" ]
then
cd $directory
# Calculate all the scores in parallel

for COLLECTION_NAME in default english stopwords
do
	for SCORER_FUNCTION in CountScorer TfIdfScorer BM25Scorer
	do
		for FIELD in text title text_and_title
		do
			echo "Using $COLLECTION_NAME with $SCORER_FUNCTION in $FIELD..."
			java homework.RunAllQueries_HW $COLLECTION_NAME $truth $SCORER_FUNCTION $FIELD output-$COLLECTION_NAME-$SCORER_FUNCTION-$FIELD.tsv > /dev/null &
		done
	done
	wait
done

cd ..
echo 
echo "Done!!"
fi
