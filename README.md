# SocketProgramming
네트워크 채팅 과제
<br><br>
## 기능
- 멀티 채팅<br>
여러 명이 동시에 채팅 가능함.
- 귓속말 <br>
사용자가 특정 사람에게만 귓속말 가능.<br>
서버가 특정 사용자에게말 귓속말 가능.<br>
사용자가 서버에게만 귓속말 가능.
- 서버 로그 수집<br>
모든 사용자의 채팅방 출입 내역, 채팅, 귓속말 내용 서버 화면에 표시
<br><br>
## 시작 방법
### main branch (Java, Intellij)
1. Clone or download zip
2. Run ChatServer.java
3. Run ChatClient.java and enter your IP address, and nickname.
4. 귓속말 기능을 사용하고 싶다면 <code>/to [nickname] [message]</code> 입력.<br>
   대괄호는 제외하고 입력, 서버의 경우 nickname은 server임.
<br><br>
### Android branch (Java, AndroidStudio)
안드로이드에는 서버가 없으므로 꼭 main branch 프로젝트의 서버를 실행시키고 진행해야 한다.
1. Clone or download zip
2. Run ChatServer.java from main branch project
3. Run Android App and enter your IP address, and nickname.<br>
*참고로 emulator localhost IP는 10.0.2.2이다.
4. 귓속말 기능은 위와 동일하다.
