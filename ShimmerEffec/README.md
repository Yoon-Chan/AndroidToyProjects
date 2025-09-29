# Shimmer UI Modifier 구현

+ **Skeleton UI**: 콘텐츠가 오기 전, 레이아웃의 뼈대(고정된 회색 블록) 를 보여 주는 패턴. 어디에 이미지/텍스트가 들어올지 자리를 미리 잡아 레이아웃 점프를 줄여요. 애니메이션은 필수 아님(대부분 정적).
+ **Shimmer UI**: 그 뼈대(placeholder) 위에 빛이 스치는 듯한 애니메이션 그라디언트를 얹어 “로드 중” 느낌을 주는 효과. 즉, 흔히 skeleton + shimmer 효과로 함께 쓰이지만, 개념적으로는 skeleton=구조, shimmer=효과.

Modifier.shimmerEffect() 함수 구현
+ IntSize로 컴포저블의 크기 계산
+ rememberInfiniteTransition()를 이용해 무한 애니메이션 적용
+ Brush를 사용한 gradient 적용

간단한 Shimmer UI 모디파이어 실행 동작

<img src="https://github.com/user-attachments/assets/b93fa597-d1c3-442e-9f0b-95613861c26a" width=300 height=600/>
