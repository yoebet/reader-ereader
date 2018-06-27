package wjy.yo.ereader.entity;

import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FetchedData {
    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    @EqualsAndHashCode.Include
    protected String id;

    @EqualsAndHashCode.Include
    protected long version;

    protected Date createdAt;

    protected Date updatedAt;

    protected Date lastFetchAt;

}
