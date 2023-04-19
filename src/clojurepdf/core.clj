(ns clojurepdf.core
  (:require [clj-pdf.core :as pdf]
            [httpkit.server :refer [run-server]]
            [clojure.data.json :as json]))

(defn new-line [pdf-page]
  )


(defn create-pdf [dados]
  (let [pdf-doc (pdf/create-pdf)
        pdf-page (pdf/start-page pdf-doc)]
    (pdf/draw-text pdf-page "Formulário" {:font-size 24 :align :center})
    (pdf/new-line pdf-page)
    (doseq [[chave valor] dados]
      (pdf/draw-text pdf-page (str chave ": " valor) {:font-size 12 :align :left})
      (pdf/new-line pdf-page))
    (pdf/save-pdf pdf-doc "formulario.pdf")
    (pdf/close-pdf pdf-doc)))

(defn index-page [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str "<html><head><title>Formulário</title></head><body>"
              "<form method=\"POST\" action=\"/submit\">"
              "Nome: <input type=\"text\" name=\"nome\"><br>"
              "Email: <input type=\"text\" name=\"email\"><br>"
              "Telefone: <input type=\"text\" name=\"telefone\"><br>"
              "<input type=\"submit\" value=\"Enviar\">"
              "</form></body></html>")})

(defn submit-page [request]
  (let [params (httpkit.params/get-params request)]
    (create-pdf params)
    {:status 200
     :headers {"Content-Type" "application/json"}
     :body (json/write-str {:mensagem "Formulário enviado com sucesso"})}))

(defn start-server []
  (run-server (fn [request]
                (case (:uri request)
                  "/" (index-page request)
                  "/submit" (submit-page request)
                  (-> {:status 404
                       :body "Página não encontrada"}
                      (assoc-in [:headers "Content-Type"] "text/plain"))))))
