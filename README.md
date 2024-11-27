# 숫자 야구 GUI
숫자 야구 게임을 Swing(GUI)으로 구현했습니다.

### 사용한거
- Look and Feel: [JFormDesigner/FlatLaf](https://github.com/JFormDesigner/FlatLaf)
- Font: [Noto Sans KR](https://fonts.google.com/noto/specimen/Noto+Sans+KR)

### 요구사항
- JVM 21 버전 이상

### 실행하기
- 릴리즈 [1.0.1](https://github.com/k-yumin/number-baseball-gui/releases/tag/1.0.1)

### 빌드하기
- `./gradlew build`
- `java -jar build/libs/baseball-1.0.1-all.jar`

## 스크린샷
|Main|Rule|Game|
|:-:|:-:|:-:|
|![Screenshot 2024-11-27 204030](https://github.com/user-attachments/assets/2020f916-020e-4dc8-a75b-52e82a0b6175)|![Screenshot 2024-11-27 204102](https://github.com/user-attachments/assets/4e7dfcf6-51b4-41e9-b253-515ba0d36c0e)|![Screenshot 2024-11-27 205108](https://github.com/user-attachments/assets/4c22fd0d-6cf0-4e1d-ae01-677f3ecde805)|
|![Screenshot 2024-11-27 204331](https://github.com/user-attachments/assets/8f03d2f8-2c9e-46a9-ac98-5ff770a0caa1)|![Screenshot 2024-11-27 204515](https://github.com/user-attachments/assets/0b7f25bc-2c5b-4c9c-a2d7-674882812f1d)|![Screenshot 2024-11-27 204416](https://github.com/user-attachments/assets/51fb4bc9-6401-4411-aa76-6560b44f6eeb)|

'내가 생각한 n자리 숫자를 컴퓨터가 맞히기' 게임에서 마우스로 직접 카운터와 상호작용하는 부분이 핵심입니다!
- 마우스를 올려놓은 위치만큼 카운터에 불이 들어옵니다. (마우스를 세 번째 원에 올려놓으면 3만큼 불이 들어옴)
- 그 상태로 클릭하면 카운트를 고정합니다. 다시 클릭해서 고정 상태를 해제할 수 있습니다.
- 잘못된 입력은 무시합니다. (예를 들어, 세 자리 수 맞추는데 2볼 2스트라이크를 표시하려는 경우 입력 무시됨)
