package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.common.settings.Settings;

import java.io.Reader;

/**
 * Created by IntelliJ IDEA.
 * User: Medcl'
 * Date: 12-5-22
 * Time: 上午10:39
 */
public final class DtmfAnalyzer extends Analyzer {


    private String padding_char;
    private String first_letter;


    public DtmfAnalyzer(Settings settings) {
        first_letter = settings.get("first_letter", "none");
        padding_char = settings.get("padding_char", "");
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
                    if (first_letter.equals("only")) {
                        return new TokenStreamComponents(new DtmfAbbreviationsTokenizer(reader));
                    } else {
                        return new TokenStreamComponents(new DtmfTokenizer(reader, padding_char, first_letter));
                    }
    }

}
