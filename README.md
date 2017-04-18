# Run first:
source set-my-classpath-homework.sh

# Then to run the homework with the cran colletion run
. inverted-index.sh --cran
. scores.sh --cran

# To run the homework with the time colletion run instead
. inverted-index.sh --time         
. scores.sh --time

# To compute the Average R- run
python raverage.py --cran

# or
python raverage.py --time

# The results are exported in the folders collection-cran and collection-time

# To clean the generated files run
./clean.sh
