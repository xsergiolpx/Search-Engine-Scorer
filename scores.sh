#!/bin/bash
# Calculate all the scores in parallel

for COLLECTION_NAME in default english stopwords
do
	for SCORER_FUNCTION in CountScorer TfIdfScorer BM25Scorer
	do
		for FIELD in text title
		do
			echo "Using $COLLECTION_NAME with $SCORER_FUNCTION in $FIELD..."
			java homework.RunAllQueries_HW $COLLECTION_NAME ./Cranfield_DATASET/cran_all_queries.tsv $SCORER_FUNCTION $FIELD output-$COLLECTION_NAME-$SCORER_FUNCTION-$FIELD.tsv > /dev/null &
		done
	done
	wait
done

echo 
echo "Done!!"
