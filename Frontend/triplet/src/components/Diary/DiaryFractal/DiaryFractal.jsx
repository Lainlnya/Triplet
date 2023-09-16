import React from 'react';
import './DiaryFractal.scss';
import { useNavigate } from 'react-router-dom';

// 다이어리 조각 작은거
export default function DiaryFractal({
  date,
  tripId,
  isValid,
  day,
  dailyInfo,
}) {
  const navigate = useNavigate();
  console.log(dailyInfo);

  return (
    <li
      className={`diaryFractal ${isValid ? '' : 'blocked'}`}
      onClick={() =>
        navigate(`/trips/${tripId}/dailies/${dailyInfo.dailyId}`, {
          state: [dailyInfo, day],
        })
      }
    >
      <div className="tripDate">{date}</div>
      {/* 지출 내역 있으면, 즉 보여줄거면 사진 크기 작게 하고 밑에 지출내역 보여주고, 아니면 이미지 크게 보여줄 것 */}
      {dailyInfo.imageUrl && <img src="" alt="thumbnail" />}
    </li>
  );
}
