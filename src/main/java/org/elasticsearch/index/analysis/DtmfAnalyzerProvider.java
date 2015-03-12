package org.elasticsearch.index.analysis;

import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.settings.IndexSettings;

/**
 */
public class DtmfAnalyzerProvider extends AbstractIndexAnalyzerProvider<DtmfAnalyzer> {

    private final DtmfAnalyzer analyzer;

    @Inject
    public DtmfAnalyzerProvider(Index index, @IndexSettings Settings indexSettings, Environment env, @Assisted String name, @Assisted Settings settings) {
        super(index, indexSettings, name, settings);
        analyzer = new DtmfAnalyzer(settings);
    }

    @Override
    public DtmfAnalyzer get() {
        return this.analyzer;
    }
}
