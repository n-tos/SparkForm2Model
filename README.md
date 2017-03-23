# SparkForm2Model
---
Spark Frameworkでfromデータからモデルへの変換処理を行うサンプルです。
spring bootでいうところの @ModelAttribute といったものはありません。
理由としては作者があくまでシンプルな実装を好んでいるからのようです。  
そこで、formデータからModelへの変換については、個々で対応が必要となります。  
アプ ーチとしては3つを考えましたので、サンプルとして置いておきます。
#### ① AjaxでJSON形式のやりとりを行う
この場合、画面遷移とformのsubumitができません。  
実際にはformでのやり取りではないため、javascriptの実装が必要になります。  

#### ② パラメータから直接Modelへ詰め込む
Sparkはマイクロフレームワークであり、REST APIとして使われることが多いようです。  
そのため、パラメータからの値の取り込みが簡単なことも特徴のひとつです。
パラメータから値を取り込んで、直接モデルへセットしています。
ただ、これはパラメータの変更があった場合や、そもそもパラメータが膨大にある場合には管理が大変となるので、使用箇所としては限られるかもしれません。

#### ③ commons-beanutilsを用いてパラメータから変換する
(参考) https://sparktutorials.github.io/2015/09/22/spark-minitwit.html  
Sparkのチュートリアルに載っている方法です。
基本的には②の内容と同じですが、Apacheのcommons-beanutilsというライブラリを用いてパラメータをまとめて処理します。  
実際にソースを見るとわかりますが、URLからエンコードした値をMultiMapに詰め、そこからModelへ変換します。
ライブラリの制限がない場合や、入力フォームが多い場合にはこちらをおすすめします。  
できればcommons-beanutilsのソースも見ておくとわかりやすいかと思います。