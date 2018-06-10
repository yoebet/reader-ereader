package wjy.yo.ereader.ui.reader;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;
import javax.inject.Singleton;

import wjy.yo.ereader.entity.book.Chap;
import wjy.yo.ereader.repository.BookRepository;

@Singleton
public class ReaderViewModel extends ViewModel {

    private final MutableLiveData<String> liveChapId;

    private final LiveData<Chap> chapWithParas;

    @Inject
    public ReaderViewModel(BookRepository bookRepository) {
        this.liveChapId = new MutableLiveData<>();
        this.chapWithParas = Transformations.switchMap(this.liveChapId, bookRepository::loadChapDetail);

        System.out.println("new ReaderViewModel: " + this);
    }

    public void setChapId(String id) {
        this.liveChapId.setValue(id);
    }

    public LiveData<Chap> getChapWithParas() {
        return chapWithParas;
    }
}
