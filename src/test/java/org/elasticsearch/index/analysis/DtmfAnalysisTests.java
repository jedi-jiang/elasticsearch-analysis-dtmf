/*
* Licensed to ElasticSearch and Shay Banon under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership. ElasticSearch licenses this
* file to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package org.elasticsearch.index.analysis;

import junit.framework.Assert;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.elasticsearch.common.inject.Injector;
import org.elasticsearch.common.inject.ModulesBuilder;
import org.elasticsearch.common.settings.SettingsModule;
import org.elasticsearch.env.Environment;
import org.elasticsearch.env.EnvironmentModule;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.IndexNameModule;
import org.elasticsearch.index.settings.IndexSettingsModule;
import org.elasticsearch.indices.analysis.IndicesAnalysisService;
import org.hamcrest.MatcherAssert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.common.settings.ImmutableSettings.Builder.EMPTY_SETTINGS;
import static org.hamcrest.Matchers.instanceOf;

/**
 */
public class DtmfAnalysisTests {

    @Test
    public void testDtmfAnalysis() {
        Index index = new Index("test");

        Injector parentInjector = new ModulesBuilder().add(new SettingsModule(EMPTY_SETTINGS), new EnvironmentModule(new Environment(EMPTY_SETTINGS))).createInjector();
        Injector injector = new ModulesBuilder().add(
                new IndexSettingsModule(index, EMPTY_SETTINGS),
                new IndexNameModule(index),
                new AnalysisModule(EMPTY_SETTINGS, parentInjector.getInstance(IndicesAnalysisService.class)).addProcessor(new DtmfAnalysisBinderProcessor()))
                .createChildInjector(parentInjector);

        AnalysisService analysisService = injector.getInstance(AnalysisService.class);


        TokenizerFactory tokenizerFactory = analysisService.tokenizer("dtmf");
        MatcherAssert.assertThat(tokenizerFactory, instanceOf(DtmfTokenizerFactory.class));

        TokenFilterFactory tokenFilterFactory = analysisService.tokenFilter("dtmf");
        MatcherAssert.assertThat(tokenFilterFactory,instanceOf(DtmfTokenFilterFactory.class));
    }

    @Test
    public void testTokenFilter() throws IOException{
        StringReader sr = new StringReader("刘德华");
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_41);
        DtmfTokenFilter filter = new DtmfTokenFilter(analyzer.tokenStream("f",sr),"","none");
        List<String>  dtmf= new ArrayList<String>();
        filter.reset();
        while (filter.incrementToken())
        {
            CharTermAttribute ta = filter.getAttribute(CharTermAttribute.class);
            dtmf.add(ta.toString());
        }
        Assert.assertEquals(3, dtmf.size());
        System.out.println(dtmf.get(0));
        System.out.println(dtmf.get(1));
        System.out.println(dtmf.get(2));
        Assert.assertEquals("548", dtmf.get(0));
        Assert.assertEquals("33",dtmf.get(1));
        Assert.assertEquals("482", dtmf.get(2));

        sr = new StringReader("刘德华");
        analyzer = new KeywordAnalyzer();
        filter = new DtmfTokenFilter(analyzer.tokenStream("f",sr),"","only");
        filter.reset();
        dtmf.clear();
        while (filter.incrementToken())
        {
            CharTermAttribute ta = filter.getAttribute(CharTermAttribute.class);
            dtmf.add(ta.toString());
        }
        Assert.assertEquals(1, dtmf.size());
        Assert.assertEquals("534",dtmf.get(0));

        sr = new StringReader("CfilOsvz");
        analyzer = new KeywordAnalyzer();
        filter = new DtmfTokenFilter(analyzer.tokenStream("f",sr),"","none");
        filter.reset();
        dtmf.clear();
        while (filter.incrementToken())
        {
            CharTermAttribute ta = filter.getAttribute(CharTermAttribute.class);
            dtmf.add(ta.toString());
        }
        Assert.assertEquals(1, dtmf.size());
        Assert.assertEquals("23456789",dtmf.get(0));
    }

    @Test
    public void testTokenizer() throws IOException {
        String[] s = {"刘德华", "劉德華", "刘德华A1", "刘德华A2","讲话频率小，不能发高音","T波低平或倒置","β-氨基酸尿"};
        for (String value : s) {
            System.out.println(value);
            StringReader sr = new StringReader(value);

            DtmfTokenizer tokenizer = new DtmfTokenizer(sr," ","none");
//            DtmfTokenizer tokenizer = new DtmfTokenizer(sr, " ", "only");
//            DtmfTokenizer tokenizer = new DtmfTokenizer(sr," ","prefix");
//              DtmfTokenizer tokenizer = new DtmfTokenizer(sr," ","append");
//            DtmfAbbreviationsTokenizer tokenizer = new DtmfAbbreviationsTokenizer(sr);

            tokenizer.reset();
            boolean hasnext = tokenizer.incrementToken();

            while (hasnext) {

                CharTermAttribute ta = tokenizer.getAttribute(CharTermAttribute.class);

                System.out.println(ta.toString());

                hasnext = tokenizer.incrementToken();

            }
        }

    }
}
