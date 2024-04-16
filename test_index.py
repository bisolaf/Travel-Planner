import pytest
from index import Indexer
import math


def test_example():
    assert 2 == 1 + 1

def test_process_document():
   test_sample = Indexer("", "", "", "")

   result = ["lago", "love"]
   resulttheirs = test_sample.process_document("Lagos", 0, "love you")
   result_einstein = ["littl", "einstein", "littl", "dog", "said"]
   assert test_sample.process_document("Lagos", 0, "love you") == result
   assert test_sample.process_document("Little Einstein", 1, "My little dog said") == result_einstein
   assert len(resulttheirs) == 2
   assert "love" in resulttheirs
   assert "lago" in resulttheirs
   assert "Lagos" not in resulttheirs
   assert "my" not in test_sample.process_document("Little Einstein", 1, "My little dog said")
    
   testtitle = "lagos town"
   testlink = "[[lagos link|(town text)]]"
   test_link = "[Nigeria Towns]"
   test_sam = Indexer("town", "visits", "lagos town in naij", "cars")
   linktocheck = test_sam.process_document(testtitle, 1, testlink)
   
   assert "lago" in linktocheck
   assert "town" in linktocheck

   linkcheck2 = linktocheck = test_sam.process_document(testtitle, 1, test_link)
   assert "nigeria" in linkcheck2
   assert "exams" not in linkcheck2

def test_compute_tf():
      test_index = Indexer("wikis/test_tf_idf.xml", "test_titles.txt",
       "test_docs.txt", "test_words.txt")
      test_index.parse()
      dict = test_index.compute_tf()
      assert dict["dog"][1] == 1.0
      assert dict["chees"][2] ==  1.0
      nigeria_index = Indexer("wikis/ExampleWiki.xml", "nigeria_titles.txt",
       "nigeria_docs.txt", "nigeria_words.txt")
      nigeria_index.parse()
      nigeria_dict = nigeria_index.compute_tf()
      assert nigeria_dict["nigeria"][1] == 0.5
      assert nigeria_dict["home"][1] == 1.0
      assert nigeria_dict.get("west") == {2: 0.3333333333333333}
      assert nigeria_dict.get("nigeria") ==  pytest.approx({1: 0.5, 2: 0.3333333333333},abs=1e-3 )
      
      with pytest.raises( KeyError):
             assert nigeria_dict["country"][1] is None
             assert nigeria_dict["home"][5] is None
             assert nigeria_dict["Home"][1] is None
             assert nigeria_dict["west"][1] == 0
             assert nigeria_dict("tsunami") is None
             assert nigeria_dict.get(90) is None
             assert nigeria_dict[0] is None
    

def test_compute_idf():
      test_index = Indexer("wikis/test_tf_idf.xml", "test_titles.txt",
       "test_docs.txt", "test_words.txt")
      test_index.parse()
      dictidf = test_index.compute_idf()
      assert dictidf["dog"] == pytest.approx(0.405, abs=1e-3)
      assert dictidf.get("bit") ==  0.4054651081081644
      nigeria_index = Indexer("wikis/ExampleWiki.xml", "nigeria_titles.txt",
       "nigeria_docs.txt", "nigeria_words.txt")
      nigeria_index.parse()
      nigeria_dict = nigeria_index.compute_idf()
      assert nigeria_dict["home"] == pytest.approx(0.693, abs=1e-3)
      assert nigeria_dict["nigeria"] == pytest.approx(math.log(1))
      assert nigeria_dict.get("west") ==  0.6931471805599453
      with pytest.raises( KeyError):
            assert nigeria_dict["was"] is None
            assert nigeria_dict["delta"] is None
            assert nigeria_dict.get("NIGERIA") is None
            assert nigeria_dict.get(90) is None
            assert nigeria_dict[8] is None




def test_compute_term_relevance():
     
      nigeria_index = Indexer("wikis/ExampleWiki.xml", "nigeria_titles.txt",
       "nigeria_docs.txt", "nigeria_words.txt")
      nigeria_index.parse()
      nigeria_dict_tv = nigeria_index.compute_term_relevance()
      assert nigeria_dict_tv["home"][1] == pytest.approx(0.693 * 1, abs=1e-3)
      assert nigeria_dict_tv.get("africa") == {2: 0.23104906018664842}
      with pytest.raises( KeyError):
            assert nigeria_dict_tv["was"][0] is None
            assert nigeria_dict_tv["delta"] == 0
            assert nigeria_dict_tv["west"][1] == 0
            assert nigeria_dict_tv.get("ice") is None
            assert nigeria_dict_tv.get(90) is None
            assert nigeria_dict_tv[9] is None
   

def test_compute_page_rank():
     test_index = Indexer("wikis/test_tf_idf.xml", "test_titles.txt",
       "test_docs.txt", "test_words.txt")
     test_rank = Indexer("wikis/PageRankWiki.xml", "rank1_titles.txt",
       "rank1_docs.txt", "rank1_docs.txt")
     test_rank2 = Indexer("wikis/PageRankExample1.xml", "rank2_titles.txt",
       "rank2_docs.txt", "rank2_docs.txt")
     test_index.parse()
     test_rank.parse()
     test_rank2.parse()
     dictpv = test_index.compute_page_rank()
     rank1 = test_rank.compute_page_rank()
     rank2 = test_rank2.compute_page_rank()
     assert dictpv.get(2) == pytest.approx(0.3333, abs=1e-3)
     assert rank1[3] == pytest.approx(0.00545, abs=1e-3)
     assert rank2[1] == pytest.approx(0.4326, abs=1e-3) 
     assert dictpv[1] == pytest.approx(0.3333, abs=1e-3)
     assert dictpv[2] == pytest.approx(0.3333, abs=1e-3)
     assert dictpv[3] == pytest.approx(0.3333, abs=1e-3)
     assert dictpv[1] == dictpv[2]
     with pytest.raises( KeyError):
            assert dictpv[4] is None



def file_as_set(filename):
    """
    Returns all of the non-empty lines in the file, as strings in a set.
    """
    line_set = set()
    with open(filename, "r") as file:
        line = file.readline()
        while line and len(line.strip()) > 0:
            line_set.add(line.strip())
            line = file.readline()
    return line_set

def test_file_contents():
    simple_index = Indexer("wikis/SimpleWiki.xml", "simple_titles.txt",
       "simple_docs.txt", "simple_words.txt")
    simple_index.run() # run the indexer to write to the files
    titles_contents = file_as_set("simple_titles.txt")
    assert len(titles_contents) == 2
    assert "200::Example page" in titles_contents
    assert "30::Page with links" in titles_contents
