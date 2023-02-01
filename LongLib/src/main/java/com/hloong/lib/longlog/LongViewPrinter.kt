package com.hloong.lib.longlog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hloong.lib.R;
import com.hloong.lib.longlog.base.LongLogConfig;
import com.hloong.lib.longlog.base.LongLogPrinter;
import com.hloong.lib.longlog.base.LongLogType;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LongViewPrinter implements LongLogPrinter {
    private RecyclerView recyclerView;
    private LogAdapter adapter;
    private LongViewPrinterProvider viewPrinterProvider;
    public LongViewPrinter(Activity activity){
        FrameLayout rootView = activity.findViewById(android.R.id.content);
        recyclerView = new RecyclerView(activity);
        adapter = new LogAdapter(LayoutInflater.from(recyclerView.getContext()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        viewPrinterProvider = new LongViewPrinterProvider(rootView,recyclerView);

    }

    public LongViewPrinterProvider getViewPrinterProvider() {
        return viewPrinterProvider;
    }

    @Override
    public void print(@NotNull LongLogConfig config, int level, String tag, @NotNull String msg) {
        adapter.addItem(new LongLogMo(System.currentTimeMillis(),level,tag,msg));
        recyclerView.smoothScrollToPosition(adapter.getItemCount()-1);
    }

    private static class LogAdapter extends RecyclerView.Adapter<LogViewHolder>{
        private LayoutInflater inflater;
        private List<LongLogMo> logs = new ArrayList<>();

        public LogAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        void addItem(LongLogMo item){
            logs.add(item);
            notifyItemChanged(logs.size()-1);
        }

        @NonNull
        @Override
        public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(R.layout.log_item,parent,false);
            return new LogViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
            LongLogMo item = logs.get(position);
            int color = getHighlightColor(item.level);
            holder.tvMessage.setTextColor(color);
            holder.tvTag.setTextColor(color);
            holder.tvTag.setText(item.getFlattened());
            holder.tvMessage.setText(item.log);
        }
        @Override
        public int getItemCount() {
            return logs.size();
        }
        /**
         * Get different colors according to log level.
         * @param logLevel
         * @return highlight color
         */
        private int getHighlightColor(int logLevel) {
            int highlight;
            switch (logLevel) {
                case LongLogType.V:
                    highlight = 0xffbbbbbb;
                    break;
                case LongLogType.D:
                    highlight = 0xffffffff;
                    break;
                case LongLogType.I:
                    highlight = 0xff6a8759;
                    break;
                case LongLogType.W:
                    highlight = 0xffbbb529;
                    break;
                case LongLogType.E:
                    highlight = 0xffff6b68;
                    break;
                default:
                    highlight = 0xffffff00;
                    break;
            }
            return highlight;
        }
    }



    private static class LogViewHolder extends RecyclerView.ViewHolder{
        TextView tvTag;
        TextView tvMessage;
        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTag = itemView.findViewById(R.id.long_log_tv_tag);
            tvMessage = itemView.findViewById(R.id.long_log_tv_message);
        }
    }
}
