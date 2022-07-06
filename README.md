# ReminderApp Task(승명님 코드리뷰)

#. 해당 앱은 '알람(reminder) 앱'입니다.

#. Feature List
----------
1. 화면
 - Home화면
 - Detail화면
 - Alarm화면


2. 각 화면에서의 기능
 - Home화면
   - '리마인더 추가' 버튼 tab시 Detail화면으로 이동
   - 사용자가 등록한 리마인더를 리스트형태로 보여줌
   - 각 리마인더에는 checkbox가 있고, 이 checkbox를 가지고 알람을 설정하고, 해제 함.
   - 각 리마인더를 선택하면, 수정을 위해, Detail화면으로 이동함
   
 - Detail화면
  - reminder의 정보(제목, 시간, 벨소리)를 설정할 수 있음
  - 'SAVE'버튼을 통해, 리마인더를 새로 생성, 기존 리마인더를 수정 가능
 
 - Alarm화면
  - 사용자가 지정한 시간이 되면 알람이 울림
  - 잠금화면에서도 알람 화면이 나와야함.
  - 'Dismiss' 버튼을 클릭하면, 알람이 해제되고, 'Home'화면으로 이동 및 해당 리마인더는 checkbox해제(알람 비활성화)
  

#. 아키텍처
---------
- Android AAC를 사용한 MVVM패턴
- Repository 패턴(여기서는 Room을 사용했습니다. remote data source는 없습니다.)
