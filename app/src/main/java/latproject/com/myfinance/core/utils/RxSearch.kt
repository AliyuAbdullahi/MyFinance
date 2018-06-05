package latproject.com.myfinance.core.utils

import android.support.v7.widget.SearchView
import rx.Observable
import rx.subjects.BehaviorSubject

class RxSearch {
    companion object {
        val subject = BehaviorSubject.create("")

        fun usingSearchView(searchView: SearchView): Observable<String> {
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    subject.onCompleted()

                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    subject.onNext(newText ?: "")

                    return false
                }
            })

            return subject as Observable<String>
        }
    }
}