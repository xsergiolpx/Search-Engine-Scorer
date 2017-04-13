package homework;


import java.util.HashSet;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.Reference2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.di.big.mg4j.document.HtmlDocumentFactory;
import it.unimi.di.big.mg4j.index.Index;
import it.unimi.di.big.mg4j.index.TermProcessor;
import it.unimi.di.big.mg4j.query.IntervalSelector;
import it.unimi.di.big.mg4j.query.QueryEngine;
import it.unimi.di.big.mg4j.query.SelectedInterval;
import it.unimi.di.big.mg4j.query.parser.SimpleParser;
import it.unimi.di.big.mg4j.search.DocumentIteratorBuilderVisitor;
import it.unimi.di.big.mg4j.search.score.CountScorer;
import it.unimi.di.big.mg4j.search.score.TfIdfScorer;
import it.unimi.di.big.mg4j.search.score.BM25Scorer;
import it.unimi.di.big.mg4j.search.score.DocumentScoreInfo;


public class RunQuery_HW {


	protected final static String[] stopword_list = {"a", "able", "about", "above", "abst", "accordance", "according", "accordingly", "across", "act", "actually", "added", "adj", "affected", "affecting", "affects", "after", "afterwards", "again", "against", "ah", "all", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "announce", "another", "any", "anybody", "anyhow", "anymore", "anyone", "anything", "anyway", "anyways", "anywhere", "apparently", "approximately", "are", "aren", "arent", "arise", "around", "as", "aside", "ask", "asking", "at", "auth", "available", "away", "awfully", "b", "back", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "begin", "beginning", "beginnings", "begins", "behind", "being", "believe", "below", "beside", "besides", "between", "beyond", "biol", "both", "brief", "briefly", "but", "by", "c", "ca", "came", "can", "cannot", "can't", "cause", "causes", "certain", "certainly", "co", "com", "come", "comes", "contain", "containing", "contains", "could", "couldnt", "d", "date", "did", "didn't", "different", "do", "does", "doesn't", "doing", "done", "don't", "down", "downwards", "due", "during", "e", "each", "ed", "edu", "effect", "eg", "eight", "eighty", "either", "else", "elsewhere", "end", "ending", "enough", "especially", "et", "et-al", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "except", "f", "far", "few", "ff", "fifth", "first", "five", "fix", "followed", "following", "follows", "for", "former", "formerly", "forth", "found", "four", "from", "further", "furthermore", "g", "gave", "get", "gets", "getting", "give", "given", "gives", "giving", "go", "goes", "gone", "got", "gotten", "h", "had", "happens", "hardly", "has", "hasn't", "have", "haven't", "having", "he", "hed", "hence", "her", "here", "hereafter", "hereby", "herein", "heres", "hereupon", "hers", "herself", "hes", "hi", "hid", "him", "himself", "his", "hither", "home", "how", "howbeit", "however", "hundred", "i", "id", "ie", "if", "i'll", "im", "immediate", "immediately", "importance", "important", "in", "inc", "indeed", "index", "information", "instead", "into", "invention", "inward", "is", "isn't", "it", "itd", "it'll", "its", "itself", "i've", "j", "just", "k", "keep	keeps", "kept", "kg", "km", "know", "known", "knows", "l", "largely", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "line", "little", "'ll", "look", "looking", "looks", "ltd", "m", "made", "mainly", "make", "makes", "many", "may", "maybe", "me", "mean", "means", "meantime", "meanwhile", "merely", "mg", "might", "million", "miss", "ml", "more", "moreover", "most", "mostly", "mr", "mrs", "much", "mug", "must", "my", "myself", "n", "na", "name", "namely", "nay", "nd", "near", "nearly", "necessarily", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "ninety", "no", "nobody", "non", "none", "nonetheless", "noone", "nor", "normally", "nos", "not", "noted", "nothing", "now", "nowhere", "o", "obtain", "obtained", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "omitted", "on", "once", "one", "ones", "only", "onto", "or", "ord", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "owing", "own", "p", "page", "pages", "part", "particular", "particularly", "past", "per", "perhaps", "placed", "please", "plus", "poorly", "possible", "possibly", "potentially", "pp", "predominantly", "present", "previously", "primarily", "probably", "promptly", "proud", "provides", "put", "q", "que", "quickly", "quite", "qv", "r", "ran", "rather", "rd", "re", "readily", "really", "recent", "recently", "ref", "refs", "regarding", "regardless", "regards", "related", "relatively", "research", "respectively", "resulted", "resulting", "results", "right", "run", "s", "said", "same", "saw", "say", "saying", "says", "sec", "section", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sent", "seven", "several", "shall", "she", "shed", "she'll", "shes", "should", "shouldn't", "show", "showed", "shown", "showns", "shows", "significant", "significantly", "similar", "similarly", "since", "six", "slightly", "so", "some", "somebody", "somehow", "someone", "somethan", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specifically", "specified", "specify", "specifying", "still", "stop", "strongly", "sub", "substantially", "successfully", "such", "sufficiently", "suggest", "sup", "sure	t", "take", "taken", "taking", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "that'll", "thats", "that've", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "thered", "therefore", "therein", "there'll", "thereof", "therere", "theres", "thereto", "thereupon", "there've", "these", "they", "theyd", "they'll", "theyre", "they've", "think", "this", "those", "thou", "though", "thoughh", "thousand", "throug", "through", "throughout", "thru", "thus", "til", "tip", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "ts", "twice", "two", "u", "un", "under", "unfortunately", "unless", "unlike", "unlikely", "until", "unto", "up", "upon", "ups", "us", "use", "used", "useful", "usefully", "usefulness", "uses", "using", "usually", "v", "value", "various", "'ve", "very", "via", "viz", "vol", "vols", "vs", "w", "want", "wants", "was", "wasnt", "way", "we", "wed", "welcome", "we'll", "went", "were", "werent", "we've", "what", "whatever", "what'll", "whats", "when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "wheres", "whereupon", "wherever", "whether", "which", "while", "whim", "whither", "who", "whod", "whoever", "whole", "who'll", "whom", "whomever", "whos", "whose", "why", "widely", "willing", "wish", "with", "within", "without", "wont", "words", "world", "would", "wouldnt", "www", "x", "y", "yes", "yet", "you", "youd", "you'll", "your", "youre", "yours", "yourself", "yourselves", "you've", "z", "zero"};
	protected final static HashSet<String> stopwords = new HashSet<String>();


	public static void main( String arg[] ) throws Exception {
		
		// collection_name, query, FilterStopwords/ConsiderStopwords, NoStemmer/UseEnglishStemmer
		
		
		//System.out.println("\n\n\n\t RunQuery_HW :)\n\n");
		
		if (arg.length != 4) {
			System.out.println("\nWrong number of arguments :\\\n");
			System.out.println("\nCorrect use:\n");
			System.out.println(" java homework.RunQuery_HW \"collection_name\" \"query\" \"scorer_function\" \"ratio_between_title_and_text\"");
			System.out.println();
			System.out.println(" Example of collection_name: \"cran\"");
			System.out.println(" Example of query: \"problem OR homework\"");
			System.out.println(" scorer_function in {\"CountScorer\", \"TfIdfScorer\", \"BM25Scorer\"}");
			System.out.println(" ratio_between_title_and_text in {\"1:1\", \"1:2\"}");
			System.out.println();
			System.out.println("  Complete Example:");
			System.out.println("   java homework.RunQuery_HW \"cran\" \"material properties of photoelastic materials\" \"TfIdfScorer\" \"1:2\"");
			System.out.println();
			return;
		}
		
		if (!arg[2].equals("CountScorer") && !arg[2].equals("TfIdfScorer") && !arg[2].equals("BM25Scorer")) {
			System.out.println();
			System.out.println(" scorer_function \"" + arg[2] + "\"" + " is not allowed.");
			System.out.println();
			System.out.println(" scorer_function in {\"CountScorer\", \"TfIdfScorer\", \"BM25Scorer\"}");
			System.out.println();
			return;
		}
		if (!arg[3].equals("1:1") && !arg[3].equals("1:2")) {
			System.out.println();
			System.out.println(" ratio_between_title_and_text \"" + arg[3] + "\"" + " is not allowed.");
			System.out.println();
			System.out.println(" ratio_between_title_and_text in {\"1:1\", \"1:2\"}");
			System.out.println();
			return;
		}
		
		
		int text_weight = 1;
		int title_weight = 1;
		if (arg[3].equals("1:2")) {
			title_weight = 2;
		}
		
		/** First we open our indices. The booleans tell that we want random access to
		 * the inverted lists, and we are going to use document sizes (for scoring--see below). */
		final Index text = Index.getInstance( arg[ 0 ] + "-text", true, true );
		final Index title = Index.getInstance( arg[ 0 ] + "-title", true, true );
		
		// Create IDs mapping
		int[] doc_id__file_id__mapping = RunQuery_HW.doc_id__file_id__mapping(arg[ 0 ]);
		
		
		/* We need a map mapping index names to actual indices. Its keyset will be used by the
		 * parser to distinguish correct index names (e.g., "text:foo title:bar"), and the mapping
		 * itself will be used when transforming a query into a document iterator. We use a handy
		 * fastutil array-based constructor. */
		Object2ReferenceOpenHashMap<String,Index> indexMap = 
			new Object2ReferenceOpenHashMap<String,Index>( new String[] { "text", "title" }, new Index[] { text, title } );
		
		/* We now need to map index names to term processors. This is necessary as any processing
		 * applied during indexing must be applied at query time, too. */
		Object2ReferenceOpenHashMap<String, TermProcessor> termProcessors = 
			new Object2ReferenceOpenHashMap<String,TermProcessor>( new String[] { "text", "title" }, new TermProcessor[] { text.termProcessor, title.termProcessor } );
		
		/* To run a query in a simple way we need a query engine. The engine requires a parser
		 * (which in turn requires the set of index names and a default index), a document iterator
		 * builder, which needs the index map, a default index, and a limit on prefix query
		 * expansion, and finally the index map. */
		QueryEngine engine = new QueryEngine(
			new SimpleParser( indexMap.keySet(), "text", termProcessors ),
			new DocumentIteratorBuilderVisitor( indexMap, text, 1000 ), 
			indexMap
			
		);

		/* Optionally, we can score the results. Here we use a state-of-art ranking 
		 * function, BM25, which requires document sizes. */
		engine.score( new BM25Scorer() );
		if (arg[2].equals("CountScorer")) {
			engine.score( new CountScorer() );
		} else if (arg[2].equals("TfIdfScorer")) {
			engine.score( new TfIdfScorer() );
		}
		
		
		
		
		/* Optionally, we can weight the importance of each index. To do so, we have to pass a map,
		 * and again we use the handy fastutil constructor. Note that setting up a BM25F scorer
		 * would give much better results, but we want to keep it simple. */
		engine.setWeights( new Reference2DoubleOpenHashMap<Index>( new Index[] { text, title }, new double[] { text_weight, title_weight } ) );
		
		/* Optionally, we can use an interval selector to get intervals representing matches. */
		engine.intervalSelector = new IntervalSelector();
		
		/* We are ready to run our query. We just need a list to store its results. The list is made
		 * of DocumentScoreInfo objects, which comprise a document id, a score, and possibly an
		 * info field that is generic. Here the info field is a map from indices to arrays
		 * of selected intervals. This part will be empty if we do not set an interval selector. */
		ObjectArrayList<DocumentScoreInfo<Reference2ObjectMap<Index, SelectedInterval[]>>> result = 
			new ObjectArrayList<DocumentScoreInfo<Reference2ObjectMap<Index,SelectedInterval[]>>>();
		
		
		
		// Automatic Selection: NoStemming/EnglishStemmer/EnglishStemmerFilteringStopwords
		String stemmer_type = text.termProcessor.toString();
		boolean filter_stopwords = false;
		if (stemmer_type.contains("EnglishStemmerStopwords")) {
			filter_stopwords = true;
			for (String stopword : RunQuery_HW.stopword_list) {
				RunQuery_HW.stopwords.add(stopword);
			}
		}
		String[] tok_query = arg[ 1 ].split(" ");
		String query = "";
		for (String tok : tok_query) {
			tok = tok.trim().toLowerCase();
			if (tok.length() == 0) {
				continue;
			}
			if (filter_stopwords) {
				if (RunQuery_HW.stopwords.contains(tok)) {
					continue;
				}
			}
			
			if (query.length() != 0) {
				query += " OR ";
			}
			query += tok;
		}
		//System.out.println("\n\n\n"+query+"\n\n\n");
		
		
		
		
		/* The query engine can return any subsegment of the results of a query. Here we grab the first 20 results. */
		engine.process( query, 0, 20, result );
		
		
		
		
		System.out.println();
		System.out.println( "DocumentID" + "\t" + "InternalDocID" + "\t" + "Score" );
		for( DocumentScoreInfo<Reference2ObjectMap<Index, SelectedInterval[]>> dsi : result ) {
			System.out.println(doc_id__file_id__mapping[(int)dsi.document] + "\t" + dsi.document + "\t" + (""+dsi.score).replace('.', ',') );
		}
		System.out.println();
		System.out.println();
		System.out.println(text.termProcessor);
		System.out.println(title.termProcessor);
		System.out.println(termProcessors);
		System.out.println();
		System.out.println("\n\n\n"+query+"\n\n\n");
	}
	
	
	
	
	
	protected static int[] doc_id__file_id__mapping(String collection_name) throws Exception {
		String file = "./"+collection_name+".collection";
		BufferedReader br = new BufferedReader(
				   new InputStreamReader(
		                      new FileInputStream(file), "UTF8"), 1000000);
		String all_content = "";
		String line = "";
		while ((line = br.readLine()) != null) {
			all_content += line;
		}
		br.close();
		
		String[] tokens = all_content.split("______");
		int num_files = 0;
		for (String tok : tokens) {
			if (tok.contains(".html")) {
				num_files++;
			}
		}
		
		int[] doc_id__file_id__mapping = new int[num_files];
		int doc_id = 0; 
		for (String tok : tokens) {
			if (tok.contains(".html")) {
				tok = tok.substring(0, tok.indexOf('.'));
				tok = tok.replace("_", "");
				doc_id__file_id__mapping[doc_id] = Integer.parseInt(tok);
				doc_id++;
			}
		}
		
		return doc_id__file_id__mapping;
	}
}
