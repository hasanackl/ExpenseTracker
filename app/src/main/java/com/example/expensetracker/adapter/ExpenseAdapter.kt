package com.example.expensetracker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.R
import com.example.expensetracker.data.Expense

class ExpenseAdapter(
    private val onDeleteClick: (Expense) -> Unit,
    private val onItemClick: (Expense) -> Unit
) : ListAdapter<Expense, ExpenseAdapter.ExpenseViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        private val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvCategoryIcon: TextView = itemView.findViewById(R.id.tvCategoryIcon)
        private val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)

        fun bind(expense: Expense) {
            tvTitle.text = expense.title
            tvCategory.text = expense.category
            tvAmount.text = "$${String.format("%.2f", expense.amount)}"
            tvDate.text = expense.date
            tvCategoryIcon.text = getCategoryEmoji(expense.category)
            tvCategoryIcon.backgroundTintList =
                android.content.res.ColorStateList.valueOf(getCategoryColor(expense.category))
            btnDelete.setOnClickListener {
                onDeleteClick(expense)
            }
            itemView.setOnClickListener {
                onItemClick(expense)
            }
        }

        private fun getCategoryEmoji(category: String): String {
            return when (category) {
                "Food" -> "🍔"
                "Transport" -> "🚌"
                "Entertainment" -> "🎬"
                "Health" -> "💊"
                else -> "💰"
            }
        }
        private fun getCategoryColor(category: String): Int {
            val colorRes = when (category) {
                "Food" -> R.color.category_food
                "Transport" -> R.color.category_transport
                "Entertainment" -> R.color.category_entertainment
                "Health" -> R.color.category_health
                else -> R.color.category_other
            }
            return itemView.context.getColor(colorRes)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Expense, newItem: Expense) =
            oldItem == newItem
    }
}