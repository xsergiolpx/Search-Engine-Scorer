import sys
import pandas as pd
from collections import OrderedDict


class FaginsAlgorithm:
    def __init__(self, total_files, k):
        self.total_files = total_files
        self.k = k

    def get_topk(self, filenames, datasets, weights):
        self.R = {}
        self.n_total_hit = 0
        self.result = []
        i = 0
        while True:
            for f_name, ds, w in zip(filenames, datasets, weights):
                item = ds.iloc[i]
                doc_id = str(item['Doc_ID'])
                if doc_id not in self.R:
                    # I AM CHANGING THIS PART
                    self.R[doc_id] = (item['Query_ID'], item['Doc_ID'], item['Rank'], w * item['Score'], f_name, 1)
                else:
                    if f_name not in self.R[doc_id][0]:
                        old_item = self.R[doc_id]
                        new_item = (f_name, 1, w * item['Score'])
                        # I AM CHANGING THIS PART
                        self.R[doc_id] = tuple(self.R[doc_id], map(my_sum, zip(old_item, new_item)))

                    # this is a total hit
                    if self.R[doc_id][1] == self.total_files:
                        self.n_total_hit += 1

                        # stop the execution when K total hit is reached
                        if self.n_total_hit == self.k:
                            self.sort_result()
                            od = OrderedDict()
                            for i in range(self.k):
                                od[self.result[i][0]] = self.result[i][1]
                            self.result = od
                            return self.result
            i += 1

    def sort_result(self):
        l = []
        for key, value in self.R.items():
            l.append((key, value))
        l = sorted(l, key=lambda x: x[1][2], reverse=True)
        self.result = l


def my_sum(t):
    # I AM CHANGING THIS PART
    return t[0] + t[1]


def main():
    k = int(sys.argv[1])
    total_files = int(sys.argv[2])
    filenames = []
    for i in range(total_files):
        filenames.append(sys.argv[3+i])
    weights_vector = []
    for i in range(total_files):
        weights_vector.append(int(sys.argv[3+total_files+i]))

    if total_files != len(filenames) != len(weights_vector):
        raise Exception("Number of files or elements in the vector of weights don't match")

    fa = FaginsAlgorithm(total_files, k)
    dfs = []
    for filename in filenames:
        dfs.append(pd.read_csv(filename, sep='\t', usecols=['Query_ID', 'Doc_ID', 'Rank', 'Score']))
    query_ids = dfs[0]['Query_ID'].unique()
    top_k = {}
    for q in query_ids:
        df_query = []
        for df in dfs:
            df_query.append(df.loc[df['Query_ID'] == q])
        top_k = fa.get_topk(filenames=filenames, datasets=df_query, weights=weights_vector)
        print(top_k)


if __name__ == '__main__':
    main()
