--貨幣表
CREATE TABLE Currency(
	id                    nvarchar(50) NOT NULL PRIMARY KEY, 
	chartName  nvarchar(30) NULL,  --圖表名稱
	code              nvarchar(30) NULL,         --貨幣代碼
	symbol         nvarchar(30) NULL,      --貨幣代號
	description nvarchar(100) NULL, --貨幣描述
	updated       datetime2(7) NULL);   --圖表更新時間
--匯率表	
CREATE TABLE ExchangeRate(
	id                    nvarchar(50) NOT NULL PRIMARY KEY,
	currency_id nvarchar(50) references currency(id),  --貨幣主鍵值
	rate                nvarchar(25) NULL,        --匯率
	rete_float     numeric(20,5) NULL,   --匯率(小數點)
	updated       datetime2(7) NULL);   --匯率更新時間
