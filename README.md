DTMF Analysis for ElasticSearch
==================================

The DTMF Analysis plugin add DTMF encode capability into elasticsearch.

This plugin use medcl's elasticsearch-analysis-pinyin plugin (https://github.com/medcl/elasticsearch-analysis-pinyin)
as code base.

The plugin includes a `dtmf` analyzer , two tokenizer: `dtmf`  `dtmf_first_letter` and a token-filter:  `dtmf` .

1.Create a index for doing some tests
<pre>
curl -XPUT http://localhost:9200/udiabon/ -d'
{
    "index" : {
        "analysis" : {
            "analyzer" : {
                "dtmf_analyzer" : {
                    "tokenizer" : "my_dtmf",
                    "filter" : ["standard"]
                }
            },
            "tokenizer" : {
                "my_dtmf" : {
                    "type" : "dtmf",
                    "first_letter" : "none",
                    "padding_char" : " "
                }
            }
        }
    }
}'
</pre>

2.Analyzing a chinese name,such as 刘德华
<pre>
http://localhost:9200/udiabon/_analyze?text=%e5%88%98%e5%be%b7%e5%8d%8e&analyzer=dtmf_analyzer
{"tokens":[{"token":"548 33 482 ","start_offset":0,"end_offset":3,"type":"word","position":1}]}
</pre>

3.Thant's all,have fun.

optional config:
the parameter `first_letter` can be set to: `prefix` , `append` , `only` and `none` ,default value is `none`

examples:
`first_letter` set to`prifix` and  `padding_char` is set to `""`
the analysis result will be:
<pre>
{"tokens":[{"token":"53454833482","start_offset":0,"end_offset":3,"type":"word","position":1}]}
</pre>

and if we set `first_letter`  to `only` ,the result will be:
<pre>
{"tokens":[{"token":"534","start_offset":0,"end_offset":3,"type":"word","position":1}]}
</pre>
also   `first_letter`  to `append`
<pre>
{"tokens":[{"token":"548 33 482 534","start_offset":0,"end_offset":3,"type":"word","position":1}]}
</pre>
