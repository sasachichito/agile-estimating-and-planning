# agile-estimating-and-planning [![Build Status](https://travis-ci.org/sasachichito/agile-estimating-and-planning.svg?branch=master)](https://travis-ci.org/sasachichito/agile-estimating-and-planning)
[<img src="https://images-fe.ssl-images-amazon.com/images/I/51A6GGDWeaL.jpg" width=250>](https://www.amazon.co.jp/dp/B00IR1HYGW/ref=dp-kindle-redirect?_encoding=UTF8&btkr=1)

## アーキテクチャ
<img src="https://raw.githubusercontent.com/wiki/sasachichito/agile-estimating-and-planning/images/architecture.png" width=300>

## ビルド&サービス起動
```
./gradlew :iteration:bootrun :release:bootrun --parallel
```

# client
Chromeから以下にアクセス  
https://sasachichito.github.io/agile-estimating-and-planning/client/  

### Usage
| Step | 項目 | 備考 |
| :--- | :--- | :--- |
| 1. | リリースプランニング | プロジェクトに対して1回実施 |
| 1.1 | 　　ストーリー見積もり・登録 ||
| 1.2 | 　　スコープ登録 ||
| 1.3 | 　　リソース登録 ||
| 1.4 | 　　プラン登録 ||
| 2. | イテレーションプランニング | プロジェクトに対してn回実施 |
| 2.1 | 　　タスク見積もり・登録 ||
| 2.2 | 　　スコープ登録 ||
| 2.3 | 　　リソース登録 ||
| 2.4 | 　　プラン登録 ||
| 3. | バーン登録 ||
| 任意 | ダッシュボード確認 ||
| 任意 | エクスポート | 登録データはサーバープロセスがkillされると失われるため <br/> エクスポートしてファイルを保存しておくこと|
| 任意 | インポート | エクスポートしたファイルをインポート <br/> ※データは上書きされる |


# release service
リリース見積もり・計画サービス  

<img src="https://raw.githubusercontent.com/wiki/sasachichito/agile-estimating-and-planning/images/release-domain-model.png" width=450>

| Plan(プラン) ||
| :--- | :--- |
| プラン | 成果物のリリース計画 |

| Period(期間) ||
| :--- | :--- |
| 期間 | プランの実行期間(スコープとリソースから導出される) |

| Scope(スコープ) ||
| :--- | :--- |
| スコープ | 顧客要求のうち実現対象とする範囲 |
| ストーリー | スコープに含まれる顧客要求の1つ |
| ストーリーポイント | ストーリーの規模を表す数値(ストーリー間で相対値となる) <br> ※利用可能な数値はフィボナッチ数列(1,2,3,5,8...)と場合によっては0 |
| 50%見積もり | 平均ケースのストーリーポイント |
| 90%見積もり | 最悪ケースのストーリーポイント |

| Resource(リソース) ||
| :--- | :--- |
| リソース | 開発チームのベロシティと費用 |
| ベロシティ | 開発チームの単位期間(1日)あたりの消費ストーリーポイント |
| コスト | 開発チームの単位期間(1日)あたりの費用 |

# iteration service
イテレーション毎の見積もり・計画サービス  

<img src="https://raw.githubusercontent.com/wiki/sasachichito/agile-estimating-and-planning/images/iteration-domain-model.png" width=450>

| Plan(プラン) ||
| :--- | :--- |
| プラン | 1イテレーションを完了する計画 |

| Period(period) ||
| :--- | :--- |
| 期間 | プランの実行期間(スコープとリソースから導出される) |

| Scope(スコープ) ||
| :--- | :--- |
| スコープ | イテレーションに含めるストーリー |
| タスク | ストーリーの実現に必要な作業 |
| 理想時間 | 何かにかかる時間のうち周辺的な作業にかかる時間を差し引いたもの <br> ※アメフトの試合時間は理想時間では60mだが現実には3h程度かかる |
| 50%見積もり | 50%の確率で作業を完了させるときにかかる理想時間 |
| 90%見積もり | 90%の確率で作業を完了させるときにかかる理想時間 |

| Resource(リソース) ||
| :--- | :--- |
| リソース | 開発チームの稼働時間と費用 |
| メンバー | 開発者 |
| 1日あたりの稼働時間 | 単位期間(1日)あたりの稼働の理想時間 <br/> ※8h勤務の開発者はおおよそ6〜7h |

# 共通用語
|||
| :--- | :--- |
| Burn(バーン) | 何らかの作業を完了すること |
| バーンダウンチャート | プランの進捗状況を示すチャート |