(ns todo_3.core
  (:require-macros [sablono.core :refer [html]])
  (:require sablono.core))

(enable-console-print!)

(println "This text is printed from src/todo_3/core.cljs. Go ahead and edit it and see reloading in action.")

(declare render-app)

;; define your app data so that it doesn't get over-written on reload
(defonce state-atom (add-watch
                      (atom {:user-input "" :todo [] :counter 0})
                      :app-state
                      (fn [key state-atom old new]
                        (do
                         (println @state-atom)
                         (render-app state-atom)))))

(defonce test-atom (atom {:user-input "test purposes" :todo [] :counter 0}))

(defn reset-state-atom [state-atom]
  (reset! state-atom {:user-input "" :todo [] :counter 0}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;; Actions ;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;--user-input---
(defn input-change [app-state input] (merge app-state {:user-input input}))

(defn input-clear [app-state input] (merge app-state {:user-input ""}))


;;--todo---
(defn add-todo [app-state] (merge app-state {:todo (conj (:todo app-state) { :id (:counter app-state) :task (:user-input app-state) :done false})}))

(defn mark-todo [state-atom id] (merge @state-atom {:todo (map #(do (if (= id (:id %)) (merge % {:done true}) %)) (:todo @state-atom))}))

(defn delete-todo [state-atom id] (merge @state-atom {:todo (filterv #(not= (:id %) id) (:todo @state-atom))}))


;;--counter--
(defn up-count [app-state] (merge app-state { :counter (inc (:counter app-state))}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;; UI ;;;;;;;;;;;;;;;;;;;;;;;:
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; (defn br [] (html [:br]))
(defn title [] (html [:h1 {:style {:background-color "#F8F5EC"}} "Todo App!"]))

(defn user-input [state-atom]
  (html [:input
         {:placeholder "Enter new task..."
          :value (:user-input @state-atom)
          :on-change #(swap! state-atom input-change (.-value(.-target %)))
          :on-key-up
           #(if (= (.-keyCode %) 13)
             (do
              (swap! state-atom up-count)
              (swap! state-atom add-todo)
              (swap! state-atom input-clear)))}]))

;; Root component!
(defn root [state-atom]
  (html [:div {}
         (title)
         (user-input state-atom)]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;:; Render ;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn render-app [state-atom]
  (.render js/ReactDOM
   (root state-atom)
   (.getElementById js/document "app")))


(defn on-js-reload []
  (render-app state-atom))
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;; Action Tests ;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn run-test [test-atom]
  (do
    (println ">>>>>>>>> Starting test on: add-todo >>>>>>>>>")
    (reset! test-atom {:user-input "test purposes" :todo []})
    ; (if
    ;   (= (add-todo test-atom {:test "test-map"}) {:user-input "test purposes" :todo [{:test "test-map"}]})
    ;   (println "--> Result (input-todo): Passed!")
    ;   (println "--> Result (input-todo): Failed!"))
    (println ">>>>>>>>> Starting test on: mark-todo >>>>>>>>>")
    (println "...Inserting {:id 0 :todo \"todo1\" :done false }...")
    (reset! test-atom {:user-input "Hello World" :todo [{:id 0 :done false} {:id 1 :done false}]})
    (if
     (= (mark-todo test-atom 0) {:user-input "Hello World" :todo [{:id 0 :done true} {:id 1 :done false}]})
     (println "--> Result (mark-todo): Passed!")
     (println "--> Result (mark-todo): Failed!"))
    (println ">>>>>>>>> Starting test on: delete-todo >>>>>>>>>")
    (reset! test-atom {:user-input "Hello World" :todo [{:id 0 :done false} {:id 1 :done false}]})
    (if
      (= (delete-todo test-atom 0) {:user-input "Hello World" :todo [{:id 1 :done false}]})
      (println "--> Result (delete-todo): Passed!")
      (println "--> Result (delete-todo): Failed!"))
    (println ">>>>>>>>> Starting test on: input-change >>>>>>>>>")
    (reset! test-atom {:user-input "Hello World" :todo []})
    (if
       (= (input-change @test-atom "testing 123...") {:user-input "testing 123..." :todo []})
       (println "--> Result (input-change): Passed!")
       (println "--> Result (input-change): Failed!"))
    (println "<<<<<< Test Ended <<<<<<")))
