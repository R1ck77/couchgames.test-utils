(ns couchgames.test-utils-internal-test
  (:require [clojure.test :refer :all]
            [couchgames.test-utils]))

(deftest test-same-unordered?
  (testing "no duplicates"
    (is (#'couchgames.test-utils/same-unordered? [1 2 3 4] [1 2 3 4]))
    (is (#'couchgames.test-utils/same-unordered? [1 2 3 4] [1 3 2 4]))
    (is (#'couchgames.test-utils/same-unordered? [1 2 3 4] [1 3 2 4]))
    (is (#'couchgames.test-utils/same-unordered? [4 3 2 1] [1 3 2 4]))
    (is (#'couchgames.test-utils/same-unordered? [4 3 2 1] [1 3 2 4])))
  (testing "with duplicates"
    (is (not (#'couchgames.test-utils/same-unordered? [1 2 3 4] [1 1 2 3 4])))
    (is (not (#'couchgames.test-utils/same-unordered? [1 1 2 3 4] [1 2 3 4])))
    (is (#'couchgames.test-utils/same-unordered? [1 1 2 2 3 4] [1 2 1 3 2 4]))))
