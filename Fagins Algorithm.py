import sys
import multiprocessing as mp
import random
import time
import pandas as pd


class FaginsAlgorithm:
    def __init__(self, total_files, k, queue):
        self.total_files = total_files
        self.k = k
        self.result = {}
        self.n_perfect_match = 0
        self.queue = queue

    def get_topk(self, index_file, weight):
        while True:
            # Select randomly a line of the index file
            random_line = index_file.iloc[[random.randint(0, index_file.shape[0])]]

            # if the docid is already in the dict, then update the list of files that it is in
            docid = random_line['Doc_ID'].values[0]
            if docid in self.result:
                temp = self.result[docid]
                self.result[docid] = (temp[0], temp[1] + 1, self.calculate_total_score(temp[2], random_line['Score'].values[0], weight),temp[3]+str(weight))
            else:
                # (the entire line, number of matches, score)
                self.result[docid] = (random_line, 1, random_line['Score'].values[0], str(weight))

            # it means that the docid is present in all indexes
            if self.result[docid][1] == self.total_files:
                self.n_perfect_match += 1

                if self.n_perfect_match == self.k:
                    self.sort_result()
                    self.queue.put(self.result[:self.k])
                    return self.result[:self.k]

    def calculate_total_score(self, previous_score, new_score, weight):
        return previous_score + weight*new_score

    def sort_result(self):
        l = []
        for _, value in self.result.items():
            l.append(value)
        self.result = sorted(l, key=lambda x: x[2], reverse=True)


def main():
    k = int(sys.argv[1])
    total_files = int(sys.argv[2])
    index_filenames = []
    for i in range(total_files):
        index_filenames.append(sys.argv[3+i])
    weights_vector = []
    for i in range(total_files):
        weights_vector.append(int(sys.argv[3+total_files+i]))

    if total_files != len(index_filenames) != len(weights_vector):
        raise Exception("Number of files or elements in the vector of weights don't match")

    index_files = []
    for filename in index_filenames:
        f = pd.read_csv(filename, sep='\t', usecols=['Query_ID', 'Doc_ID', 'Rank', 'Score'])
        index_files.append(f)

    random.seed(time.time())

    # Define an output queue
    queue = mp.Queue()
    with mp.Manager() as manager:
        fa = manager.FaginsAlgorithm(total_files, k, queue)
    #fa = FaginsAlgorithm(total_files, k, queue)

    # Setup a list of processes that we want to run
    processes = []
    for i in range(total_files):
        processes.append(mp.Process(target=fa.get_topk, args=(index_files[i], weights_vector[i])))

    for p in processes:
        p.start()

    # Exit the completed processes
    for p in processes:
        p.join()

    # Get process results from the output queue
    results = [queue.get() for p in processes]
    #results = queue.get()

    print(results)


if __name__ == '__main__':
    main()