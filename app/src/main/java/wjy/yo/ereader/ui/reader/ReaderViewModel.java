package wjy.yo.ereader.ui.reader;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;
import javax.inject.Singleton;

import wjy.yo.ereader.entityvo.book.ChapDetail;
import wjy.yo.ereader.service.BookService;

@Singleton
public class ReaderViewModel extends ViewModel {

    private final MutableLiveData<String> liveChapId;

    private final LiveData<ChapDetail> chapWithParas;

    @Inject
    public ReaderViewModel(BookService bookService) {
        this.liveChapId = new MutableLiveData<>();
        this.chapWithParas = Transformations.switchMap(this.liveChapId, bookService::loadChapDetail);

        System.out.println("new ReaderViewModel: " + this);
    }

    public void setChapId(String id) {
        this.liveChapId.setValue(id);
    }

    public LiveData<ChapDetail> getChapWithParas() {
        return chapWithParas;
    }
}
