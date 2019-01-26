(ns couchgames.grouping-test
  (:require [clojure.test :refer :all]
            [couchgames.grouping :refer :all]))

(deftest test-permute
  (testing "empty list"
    (is (thrown? IllegalArgumentException (permute []))))
  (testing "single element list"
    (is (= [[:a]]
           (permute [:a]))))
  (testing "two elements list"
    (is (= [[:a :b] [:b :a]]
           (permute [:a :b])))))


