package br.com.ilhasoft.voy.ui.report.detail.holder

import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.databinding.ItemIndicatorBinding
import br.com.ilhasoft.voy.models.Indicator
import br.com.ilhasoft.voy.ui.report.detail.ReportDetailPresenter

/**
 * Created by jones on 12/27/17.
 */
class IndicatorViewHolder(val binding: ItemIndicatorBinding,
                          val presenter: ReportDetailPresenter) : ViewHolder<Indicator>(binding.root) {

    init {
        binding.isSelected = false
        binding.presenter = presenter
    }

    override fun onBind(indicator: Indicator) {
        configIndicatorPosition(indicator)
        binding.indicator = indicator
    }

    private fun configIndicatorPosition(indicator: Indicator) {
        binding.isSelected = presenter.indicator.selected == indicator.selected
    }

}