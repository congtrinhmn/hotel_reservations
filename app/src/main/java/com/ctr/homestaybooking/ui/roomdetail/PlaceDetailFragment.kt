package com.ctr.homestaybooking.ui.roomdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseFragment
import com.ctr.homestaybooking.data.source.PlaceRepository
import com.ctr.homestaybooking.extension.*
import com.ctr.homestaybooking.ui.booking.BookingActivity
import com.ctr.homestaybooking.util.DateUtil
import com.ctr.homestaybooking.util.parseToString
import kotlinx.android.synthetic.main.fragment_place_detail.*
import kotlinx.android.synthetic.main.layout_item_review.*
import kotlinx.android.synthetic.main.layout_place_amenity.*
import kotlinx.android.synthetic.main.layout_place_detail.*
import kotlinx.android.synthetic.main.layout_place_review.*
import kotlinx.android.synthetic.main.layout_place_thumbnail.*
import java.util.*

/**
 * Created by at-trinhnguyen2 on 2020/06/11
 */
class PlaceDetailFragment : BaseFragment() {
    private lateinit var viewModel: PlaceDetailVMContract
    private var startDate: Calendar? = null
    private var endDate: Calendar? = null

    companion object {
        fun newInstance() = PlaceDetailFragment()

        private const val KEY_PLACE_ID = "key_place_id"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = PlaceDetailViewModel(PlaceRepository())
        initRecyclerView()
        initView()
        initListener()
    }

    private fun initRecyclerView() {
        recyclerViewPromos.let {
            it.setHasFixedSize(true)
            it.adapter = PromoAdapter(viewModel.getPromos())
        }
        recyclerViewAmenities.let {
            it.setHasFixedSize(true)
            it.adapter = AmenityAdapter(viewModel.getAmenities())
        }
    }

    override fun isNeedPaddingTop() = true

    private fun initView() {
        activity?.intent?.getIntExtra(KEY_PLACE_ID, 0)?.let { placeId ->
            viewModel.getPlaceDetail(placeId).observeOnUiThread().subscribe({ placeDetailResponse ->
                placeDetailResponse.placeDetail?.let { placeDetail ->
                    placeDetail.images?.let {
                        context?.let { context ->
                            Glide.with(context).load(it[0]).into(imgBanner1)
                            Glide.with(context).load(it[1]).into(imgBanner2)
                            Glide.with(context).load(it[2]).into(imgBanner3)
                            Glide.with(context).load(it[3]).into(imgBanner4)
                            Glide.with(context).load(placeDetail.hostDetail?.imageUrl).into(imgHost)
                            Glide.with(context).load(placeDetail.reviews?.firstOrNull()?.userImage)
                                .into(imgReviewUser)
                        }
                    }

                    tvName.text = placeDetail.name
                    tvAddress.text = placeDetail.address
                    tvPlaceType.text = placeDetail.placeType
                    tvHostName.text = "chủ nhà ${placeDetail.hostDetail?.firstName}"
                    tvPlaceRoom.text = placeDetail.getRooms()
                    tvDescription.text = placeDetail.description
                    recyclerViewPromos.adapter?.notifyDataSetChanged()
                    recyclerViewAmenities.adapter?.notifyDataSetChanged()
                    ratingBar.rating = placeDetail.rateAverage.toFloat()
                    tvPlacePrice.text = placeDetail.pricePerDay.toMoney()

                    if (placeDetail.rateCount == 0) {
                        imgRatingStar.gone()
                        tvRateAverage.gone()
                        tvRateCount.gone()
                        clReview.gone()
                    } else {
                        imgRatingStar.visible()
                        tvRateAverage.visible()
                        tvRateCount.visible()
                        clReview.visible()
                        tvRateAverage.text = placeDetail.rateAverage.format(2)
                        tvReviewRateAverage.text = placeDetail.rateAverage.format(2)
                        tvRateCount.text = "(${placeDetail.rateCount})"
                        tvReviewRateCount.text = "(${placeDetail.rateCount} đánh giá)"
                        placeDetail.reviews?.firstOrNull()?.let {
                            tvReviewUsername.text = it.userName
                            tvReviewComment.text = it.comment
                            tvTime.text = DateUtil.convert(it.createDate, DateUtil.FORMAT_DATE)
                        }
                    }
                }
            }, {
                activity?.showErrorDialog(it)
            })
        }

//
//        roomTypeStatus?.let {
//            context?.let { context ->
//                Glide.with(context).load(it.roomType.thumbnail).into(imgBanner1)
//                Glide.with(context).load(it.roomType.images[0].name).into(imgBanner2)
//                Glide.with(context).load(it.roomType.images[1].name).into(imgBanner3)
//                Glide.with(context).load(it.roomType.images[2].name).into(imgBanner4)
//            }
//
//            tvPlaceType.text = it.roomType.name
//            tvPerNight.text = getString(R.string.per_night, 1)
//            tvRoomPrice.text = it.roomType.price.toString().getPriceFormat()
//            it.roomType.capacity.let { capacity ->
//                tvGuests.text = if (capacity == 1) "1 Guest" else "$capacity Guests"
//            }
//            tvPlaceType.text = "Size ${it.roomType.size}㎡"

    }

    private fun initListener() {
        imgBack.onClickDelayAction {
            activity?.onBackPressed()
        }
        lnBooking.onClickDelayAction {
            BookingActivity.start(
                this,
                startDate?.parseToString() ?: Calendar.getInstance().parseToString(),
                endDate?.parseToString() ?: Calendar.getInstance().parseToString(),
                null
            )
        }
    }

//    private fun initSwipeRefresh() {
//        swipeRefresh.setColorSchemeResources(R.color.colorAzureRadiance)
//        swipeRefresh.setOnRefreshListener {
//            Handler().postDelayed({
//                swipeRefresh?.isRefreshing = false
//            }, 300L)
//            getPromos(
//                roomTypeStatus?.roomType?.id ?: -1, startDate ?: Calendar.getInstance()
//            )
//        }
//    }

//    private fun getPromos(roomTypeId: Int, startDate: Calendar) {
//        addDisposables(
//            viewModel.getPromoByRoomType(roomTypeId, startDate)
//                .observeOnUiThread()
//                .subscribe({
//                    promoResponse = it
//                    rcvSale.adapter?.notifyDataSetChanged()
//                    viewModel.getMaxPromo()?.let {
//                        val promoPercent = 100 - it.percentDiscount
//                        tvRoomPrice.text =
//                            (roomTypeStatus?.roomType?.price?.apply { Log.d("--=", "+${this}") }
//                                ?.times(promoPercent / 100.0)).toString()
//                                .getPriceFormat()
//                        tvOldPrice.visible()
//                        tvOldPrice.text =
//                            roomTypeStatus?.roomType?.price.toString().getPriceFormat()
//                    }
//                }, {
//                    activity?.showErrorDialog(it)
//                })
//        )
//    }

    override fun getProgressBarControlObservable() = viewModel.getProgressObservable()
}
