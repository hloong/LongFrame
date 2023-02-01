package com.hloong.lib.longlog

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hloong.lib.R
import com.hloong.lib.longlog.base.LongLogConfig
import com.hloong.lib.longlog.base.LongLogPrinter
import com.hloong.lib.longlog.base.LongLogType

class LongViewPrinter(activity: Activity) : LongLogPrinter {
    private var recyclerView: RecyclerView
    private var adapter: LogAdapter
    var viewPrinterProvider: LongViewPrinterProvider

    init {
        val rootView = activity.findViewById<FrameLayout>(android.R.id.content)
        recyclerView = RecyclerView(activity)
        adapter = LogAdapter(LayoutInflater.from(recyclerView.context))
        val layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        viewPrinterProvider = LongViewPrinterProvider(rootView, recyclerView)
    }

    override fun print(config: LongLogConfig, level: Int, tag: String?, msg: String) {
        adapter.addItem(LongLogMo(System.currentTimeMillis(), level, tag!!, msg))
        recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
    }

    private class LogAdapter(private val inflater: LayoutInflater) :
        RecyclerView.Adapter<LogViewHolder>() {
        private val logs: MutableList<LongLogMo> = ArrayList()
        fun addItem(item: LongLogMo) {
            logs.add(item)
            notifyItemChanged(logs.size - 1)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
            val itemView = inflater.inflate(R.layout.log_item, parent, false)
            return LogViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
            val item = logs[position]
            val color = getHighlightColor(item.level)
            holder.tvMessage.setTextColor(color)
            holder.tvTag.setTextColor(color)
            holder.tvTag.text = item.flattened
            holder.tvMessage.text = item.log
        }

        override fun getItemCount(): Int {
            return logs.size
        }

        /**
         * Get different colors according to log level.
         * @param logLevel
         * @return highlight color
         */
        private fun getHighlightColor(logLevel: Int): Int {
            val highlight: Int
            highlight = when (logLevel) {
                LongLogType.V -> -0x444445
                LongLogType.D -> -0x1
                LongLogType.I -> -0x9578a7
                LongLogType.W -> -0x444ad7
                LongLogType.E -> -0x9498
                else -> -0x100
            }
            return highlight
        }
    }

    private class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTag: TextView
        var tvMessage: TextView

        init {
            tvTag = itemView.findViewById(R.id.long_log_tv_tag)
            tvMessage = itemView.findViewById(R.id.long_log_tv_message)
        }
    }
}