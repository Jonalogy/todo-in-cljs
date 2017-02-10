## ClojureScript
1. Use only double quotes `"...""` for your string variables!
2. Do not name variables starting with `try`, it's a reserved word
3. Becareful with `swap` It takes an atom as the first argument, and does a **auto `deref`** to pass the value into the second argument which is a supposed function.

## Lein repl tips
1. use `(in-ns 'todo_3.core)` instead of `(require '[todo_3.core :as c])` You can access variables without prefixing `c/`.

## Setting up React Environment with ClojureScript
1. Remember to include your render function

  ```clojure
  (defn render-app [state-atom]
    (.render js/ReactDOM
     (root)
     (.getElementById js/document "app")))
  ```

2.
