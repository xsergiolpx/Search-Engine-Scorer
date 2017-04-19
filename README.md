# The python files are coded for python 3.x
# The steps to run the homework are the following:

# To clean al the already generated files and run everything from the begining run:
./clean.sh

# First open a terminal inside the folder that contains this file
# Run then:
source set-my-classpath-homework.sh

# Then to run the homework with the cran colletion run
. inverted-index.sh --cran

. scores.sh --cran

# To run the homework with the time colletion run instead
. inverted-index.sh --time      

. scores.sh --time

# To create the output file of the Fagin's algorithm for the cran colletion run
python Fagins-Algorithm/FaginsAlgorithm.py 5 2 collection-cran/output-stopwords-BM25Scorer-title.tsv collection-cran/output-stopwords-BM25Scorer-text.tsv 2 1 collection-cran/output-fagins.tsv

# For the time collection the Fagin's algorithm does not work since the titles do not provide useful information
# [The general syntax for FaginsAlgorithm.py is the following]
# python Fagins-Algorithm/FaginsAlgorithm.py [k] [number of files/dataset] [weight of the i-th dataframe score separete by space] [output directory]

# To run the Threashole algorithm run
python Threshold-Algorithm/ThresholdAlgorithm.py 5 2 collection-cran/output-stopwords-BM25Scorer-title.tsv collection-cran/output-stopwords-BM25Scorer-text.tsv 2 1 collection-cran/output-threshold.tsv

# The output files is exported to collection-cran and collection-time

# To compute the Average R-Precision run
python raverage.py --cran

# or
python raverage.py --time

# The results are saved in collection-cran/results-cran.tsv and colletion-time/results-time.tsv
# To see the Average R-Precision of the 9+1+1 asked files in the homework run:
cat collection-cran/results_cran.tsv | grep "text_and_title\|fagins\|threshold" > collection-cran/results_cran_11_files.tsv; cat collection-cran/results_cran_11_files.tsv

cat collection-time/results_time.tsv | grep "text_and_title\|fagins\|threshold" > collection-time/results_time_9_files.tsv; cat collection-time/results_time_9_files.tsv


# To run the average nMDCG:
python average_nMDCG_cran.py 1

python average_nMDCG_time.py 1

python average_nMDCG_cran.py 3

python average_nMDCG_time.py 3

python average_nMDCG_cran.py 5

python average_nMDCG_time.py 5

python average_nMDCG_cran.py 10

python average_nMDCG_time.py 10

# Where the last number is k, change that number to try more values of k
# The output files are exported to collection-cran and collection-time
