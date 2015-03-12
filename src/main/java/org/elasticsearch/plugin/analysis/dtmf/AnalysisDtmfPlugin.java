package org.elasticsearch.plugin.analysis.dtmf;

import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.index.analysis.DtmfAnalysisBinderProcessor;
import org.elasticsearch.plugins.AbstractPlugin;

/**
 * The DTMF Analysis plugin add DTMF encoding capability into elasticsearch.
 */
public class AnalysisDtmfPlugin extends AbstractPlugin {

    @Override
    public String name() {
        return "analysis-dtmf";
    }

    @Override
    public String description() {
        return "Chinese or English to DTMF number convert support";
    }

    public void onModule(AnalysisModule module) {
        module.addProcessor(new DtmfAnalysisBinderProcessor());
    }
}
