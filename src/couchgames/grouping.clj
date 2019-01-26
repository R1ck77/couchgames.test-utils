(ns couchgames.grouping)

(defn permute [xo]
  (if (empty? xo)
    (throw (IllegalArgumentException. "Cannot permute empty list"))
    [xo])
)
