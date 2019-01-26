(ns couchgames.test-utils-test
  (:require [clojure.test :refer :all]
            [couchgames.test-utils :refer :all]))

(deftest test-create-iterator
  (testing "empty sequence"
    (let [f (create-iterator (list))]
      (is (nil? (f)))
      (is (nil? (f)))))
  (testing "one element"
    (let [f (create-iterator (list 1))]
      (is (= 1 (f)))
      (is (nil? (f)))
      (is (nil? (f))))))

(deftest test-with-chnecked-counter
  (testing "zero executions"
    (with-checked-counter [tick 0]))
  (testing "non zero executions"
    (with-checked-counter [tick 1]
      (tick))
    (with-checked-counter [tick 2]
      (tick)
      (tick))))

(deftest test-with-no-invocations
  (testing "no invocations"
    (with-no-invocations [rand-int]
      (is (number? (rand-nth [1 2 3 4]))))))

(deftest test-with-checked-operations
  (testing "no operations expected"
    (with-checked-operations [did []]
      (+ 1 1))
    (with-checked-operations [did [] :ordered]
      (+ 1 1)))
  (testing "one operation"
    (with-checked-operations [did [:op-a]]
      (+ 2 2)
      (did :op-a)
      (+ 1 1))
    (with-checked-operations [did [:op-a] :ordered]
      (+ 2 2)
      (did :op-a)
      (+ 1 1)))
  (testing "more operations"
    (with-checked-operations [did [:op-a :op-b :op-c] :ordered]
      (+ 2 2)
      (did :op-a)
      (+ 1 1)
      (did :op-b)
      (did :op-c))
    (with-checked-operations [did [:op-a :op-b :op-c]]
      (+ 2 2)
      (did :op-c)
      (+ 1 1)
      (did :op-a)
      (did :op-b))))



