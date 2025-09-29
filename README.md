# AndroidToyProjects
안드로이드 개발에 대한 이것저것 실험하기


## 1. ViewPager UI 다뤄보기

### 1-1 이커머스 광고 인피니티 페이저 구현

LaunchedEffect를 이용해 자동 화면 이동 구현
또한 pointerInput을 이용해 눌려진 상태에서는 자동 화면 이동이 정지되며, 다시 떼어진 경우 동작하도록 구현

<img src="https://github.com/user-attachments/assets/aaa31791-678a-44b5-8735-e124392987da" witdh=300 height=500 />

### 2-2 소개팅 포카 처럼 회전하는 페이저 구현

양 옆에 이미지가 보이도록 구현.
graphicsLayer을 이용해 현재 페이지 기준 화면 회전 및 투명도 설정

<img src="https://github.com/user-attachments/assets/dc9189e4-aac3-4a2c-a065-87ce9fc42813" witdh=300 height=500 />


---

## 2. 채팅 기능 구현

### 사용한 라이브러리
+ Compose UI
+ Ktor
+ Krossbow
+ Hilt
+ dataStore
+ serialization
+ ksp
+ timber

### 주요 기능

#### 1. 로그인 기능 구현(accessToken 발급)
#### 2. 채팅 기능 구현

|채팅1|채팅2|
|:--:|:--:|
|![chat1](https://github.com/user-attachments/assets/3b7dbe09-8cc1-4a1e-976b-54eafb3ecade)|![chat2](https://github.com/user-attachments/assets/0a1907f6-2df6-4917-a64a-0f5348f4bb7b)|

---

## 3. Shimmer UI 만들어보기

+ **Skeleton UI**: 콘텐츠가 오기 전, 레이아웃의 뼈대(고정된 회색 블록) 를 보여 주는 패턴. 어디에 이미지/텍스트가 들어올지 자리를 미리 잡아 레이아웃 점프를 줄여요. 애니메이션은 필수 아님(대부분 정적).
+ **Shimmer UI**: 그 뼈대(placeholder) 위에 빛이 스치는 듯한 애니메이션 그라디언트를 얹어 “로드 중” 느낌을 주는 효과. 즉, 흔히 skeleton + shimmer 효과로 함께 쓰이지만, 개념적으로는 skeleton=구조, shimmer=효과.

### Shimmer UI Modifier 구현
Modifier.shimmerEffect() 함수 구현
+ IntSize로 컴포저블의 크기 계산
+ rememberInfiniteTransition()를 이용해 무한 애니메이션 적용
+ Brush를 사용한 gradient 적용

간단한 Shimmer UI 모디파이어 실행 동작

<img src="https://github.com/user-attachments/assets/b93fa597-d1c3-442e-9f0b-95613861c26a" width=300 height=600/>
