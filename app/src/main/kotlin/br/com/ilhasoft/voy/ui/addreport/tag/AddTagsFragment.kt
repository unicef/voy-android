package br.com.ilhasoft.voy.ui.addreport.tag

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.ilhasoft.support.recyclerview.adapters.AutoRecyclerAdapter
import br.com.ilhasoft.support.recyclerview.adapters.OnCreateViewHolder
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.FragmentAddThemeBinding
import br.com.ilhasoft.voy.databinding.ItemTagThemeBinding
import br.com.ilhasoft.voy.models.Tag
import br.com.ilhasoft.voy.ui.addreport.ReportViewModel
import br.com.ilhasoft.voy.ui.base.BaseFragment
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class AddTagsFragment : BaseFragment() {

    companion object {
        const val TAG = "Theme"
    }

    private val binding: FragmentAddThemeBinding by lazy {
        FragmentAddThemeBinding.inflate(LayoutInflater.from(context))
    }

    private val reportViewModel by lazy { ViewModelProviders.of(activity).get(ReportViewModel::class.java) }

    private val tagsAdapter: AutoRecyclerAdapter<Tag, TagViewHolder> by lazy {
        AutoRecyclerAdapter<Tag, TagViewHolder>(tagsViewHolder).apply {
            setHasStableIds(true)
        }
    }
    private val tagsViewHolder: OnCreateViewHolder<Tag, TagViewHolder> by lazy {
        OnCreateViewHolder { layoutInflater, parent, _ ->
            TagViewHolder(ItemTagThemeBinding.inflate(layoutInflater, parent, false), reportViewModel)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupTagsRecyclerView()
        reportViewModel.getAllTags().observe(activity, Observer { list ->
            list?.let { setTags(it) }
        })
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        reportViewModel.setButtonEnable(true)
        reportViewModel.setButtonTitle(R.string.send_report)
    }

    private fun setTags(tagsList: List<Tag>) {
        tagsAdapter.clear()
        tagsAdapter.addAll(tagsList)
    }

    private fun setupTagsRecyclerView() = with(binding.tags) {
        layoutManager = setupLayoutManager()
        setHasFixedSize(true)
        adapter = tagsAdapter
    }

    private fun setupLayoutManager(): RecyclerView.LayoutManager? {
        val layoutManager = FlexboxLayoutManager(context, FlexDirection.ROW, FlexWrap.WRAP)
        layoutManager.justifyContent = JustifyContent.FLEX_START
        return layoutManager
    }

}
