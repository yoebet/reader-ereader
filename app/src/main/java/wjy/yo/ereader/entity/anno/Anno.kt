package wjy.yo.ereader.entity.anno

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

import wjy.yo.ereader.entity.Ordered

@Entity(tableName = "annotation")
class Anno : Ordered {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = 0

    var name: String? = null

    var nameEn: String? = null

    var dataValue: String? = null

    @ForeignKey(entity = AnnoGroup::class, parentColumns = arrayOf("id"), childColumns = arrayOf("groupId"), onDelete = ForeignKey.CASCADE)
    var groupId: String? = null

    override var no: Long = 0

    @Ignore
    var group: AnnoGroup? = null


    override fun toString(): String {
        return "\t\t$nameEn $name $dataValue\n"
    }

}
