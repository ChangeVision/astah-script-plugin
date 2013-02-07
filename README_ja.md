Astah Script Plugin
=============================
![dialog image](https://github.com/ChangeVision/astah-script-plugin/raw/master/doc/screenshots/script_dialog_ja.png)

バージョン
------------
1.0.0

対象エディション
------------------
Astah Community, UML, and Professional 6.5.x 以降
サンプルスクリプトの一部は、Astah UML、Astah Professionalのみで利用可能。
Astah: http://astah.change-vision.com/ja/

概要
------------
スクリプト言語でAstahにアクセスできます。ECMAScript(JavaScript)の編集と実行が可能です。

インストールの流れ
------------
1. [Download](http://cdn.change-vision.com/plugins/astah_script_plugin-1.0.0.zip)から zipファイルをダウンロードし、任意のフォルダへ展開します。

    例) Astah Professional, Windows: `$USER_HOME/.astah/professional/plugins/`,  `C:\Program Files\astah-professional\plugins\`
    Astah Professional, Mac OS X: `/Applications/astah professional/plguins/`
    
    例) Astah Community, Windows: `$USER_HOME/.astah/community/plugins/`, `C:\Program Files\astah-community\plugins\`
    Astah Community, Mac OS X: `/Applications/astah community/plguins/`

2. Astahを起動します。

3. 上部メニュー[ツール]配下に [スクリプト]が追加されています。

![menu image](https://github.com/ChangeVision/astah-script-plugin/raw/master/doc/screenshots/script_plugin_menu_ja.png)

使い方
------------

1. Astahを起動し、スクリプトを実行したいプロジェクトファイル(.asta)を開きます。
2. Astah上部メニュー[ツール]-[スクリプト]を選択すると、新規スクリプトダイアログが開きます。
3. 上段にスクリプトを入力するか、またはスクリプトダイアログのメニュー[ファイル]-[開く]を選択し、予め用意しておいたJavaScriptのファイルを開きます。  
   サンプルスクリプトは後者の方法でお試しください。
4. スクリプトダイアログのメニュー[アクション]-[実行]を選択します。(ショートカットキー [Ctrl+R]でも実行できます)
5. 下段に結果が表示されます。
6. 上段に入力したスクリプトは [ファイル]-[名前を付けて保存]で保存できます。


サンプルスクリプト
---------------------
`sample_scripts` フォルダには、合計14ケのJavaScriptサンプルファイルが格納されています。

 * addSetterGetter.js
 * addStereotypeToSelectedModel.js
 * checkEdition.js
 * countClasses.js
 * createAndOpenDiagram.js
 * createEREntities.js
 * exportCsv.js
 * printClasses.js
 * printERIndex.js
 * printMindmapTopics.js
 * printPackageAndClassInfo.js
 * printPresentationProperties.js
 * searchAndEdit.js
 * useJavaGUI.js

例) JavaScript: `printClasses.js`
```javascript
importPackage(com.change_vision.jude.api.inf.model);
var classes = astah.findElements(IClass);
for(var i in classes) {
    println(classes[i].getName());
}
```
 * 定義済みの変数を使えます。
   * `projectAccessor`
     * It's an object of ProjectAccessor in Astah API.
     * `null` if Astah has no project.
   * `astah`
     * It's same as `projectAccessor`.
   * `astahWindow`
     * Astahメインウィンドウオブジェクト
   * `scriptWindow`
     * It's the window object of the script plugin.
 * スクリプトで Astah APIを使えます。
   * Astah API 概要
     * <http://astah.change-vision.com/ja/astah-api.html>
   * Astah API 利用ガイド (Javadoc)
     * <http://members.change-vision.com/javadoc/astah-api/latest/api/en/doc/javadoc/index.html>
 * You are able to use the Java API in your script.

ビルド
------------
1. Astah Plug-in SDKをインストールします。 - <http://astah.change-vision.com/ja/plugins.html>
2. `git clone git://github.com/ChangeVision/astah-script-plugin.git`
3. `cd script`
4. `astah-build`
5. `astah-launch`

 * Generating config to load classpath [for Eclipse](http://astah.net/tutorials/plug-ins/plugin_tutorial_en/html/helloworld.html#eclipse)

      * `astah-mvn eclipse:eclipse`

他のスクリプト言語を使う
------------
OSGi JSR223準拠の他のスクリプト言語を使用できます。

1. 使用したいスクリプト言語の jarファイルをダウンロードします。(例. groovy-all.jar, jruby-###.jar)
2. Astah plugins フォルダに 1のjarファイルをコピーします。(~/.astah/plugins)
3. Astahを起動します

License
------------
Copyright 2012 Change Vision, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this work except in compliance with the License.
You may obtain a copy of the License in the LICENSE file, or at:

   <http://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Third party software licenses
------------
 * RSyntaxTextArea is licensed under modified BSD License.  Please see the included license file.
 * AutoComplete is licensed under modified BSD License.  Please see the included license file.

