(ns couchgames.test-utils
  (:require [clojure.test :refer :all]))

(defn create-iterator
  "Returns a function that iterates over the elements and then returns identically nil

  Very useful for testing"
  [elements]
  (let [remaining (atom elements)]
    (fn [& _]
      (let [next (first @remaining)]
        (swap! remaining rest)
        next))))

(defmacro with-checked-counter
  "Run the code with a counter "
  [[inc-function-name expected] & forms]
  `(let [counter# (atom 0)
         ~inc-function-name #(swap! counter# inc)]
     (let [result# (do ~@forms)]
       (is (= ~expected (deref counter#))
           (format "invocations of %s should match the expected value" (str ~inc-function-name)))
       result#)))

(defn- counted-values [list]
  (reduce (fn [acc value]
            (update acc value #(if (nil? %) 1 (inc %))))
          {}
          list))

(defn- same-unordered? [expected actual]
  (= (counted-values expected)
     (counted-values actual)))

(defn- ordered-check? [opt-args]
  (let [is-ordered  (first opt-args)]
    (= is-ordered :ordered)))

(defn compare-operation [opt-args]
  (if (ordered-check? opt-args) = same-unordered?))

(defmacro with-checked-operations
  "Run the code and check that a set of operations is performed, like:

  (with-checked-operations [did-it [:a :b :c] :ordered]
    (did-it :a)
    (did-it :b)
    (did-it :c))"
  [[update-function-name expected & ordered] & forms]
  `(let [acc# (atom [])
         ~update-function-name #(swap! acc# (fn [v#] (conj v# %)))]
     (let [result# (do ~@forms)]       
       (is ((compare-operation (quote ~ordered)) ~expected (deref acc#)) 
           (format "invocation of %s should trigger the expected values, got %s instead" (str ~update-function-name) (deref acc#)))
       result#)))

(defn create-functions-redefs [functions]
  (mapcat (fn [function]
            (list function `(fn [& optional#]
                              (is false (format "Unexpected invocation of %s" ~function)))))
          functions))

(defmacro with-no-invocations
  "Ensure that none of the functions in the array are called.

Used like this:

(with-no-invocations [android/foo-function bar/baz-function function]  
  forms)

  Returns the last form result"
  [functions & forms]
  `(with-redefs [~@(create-functions-redefs functions)]
     ~@forms))

(defn fail [message]
  (assert false message))
