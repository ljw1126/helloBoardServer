from locust import HttpUser, task, between
import random 

class AddPosts(HttpUser):
    # 각 사용자가 1 또는 2초 대기 
    wait_time = between(1, 2)

    # 각 사용자가 테스트 시작시 최초 한번 실행
    def on_start(self):
      self.client.post("/users/sign-in", json = {
        "userId" : "tester", "password" : "123"
      })

    # Locust에서 테스트 하고자 하는 작업 정의
    @task
    def add_post(self):
      self.client.post("/post", json = {
         "name" : "테스트 게시글" + str(random.randint(1, 100000)),
         "contents" : "테스트 컨텐츠" + str(random.randint(1, 100000)),
         "categoryId" : random.randint(1, 10)
      })
