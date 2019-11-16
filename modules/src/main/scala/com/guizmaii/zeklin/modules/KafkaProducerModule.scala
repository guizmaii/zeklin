package com.guizmaii.zeklin.modules

import zio.Task

trait KafkaProducerModule extends Serializable {
  def kafkaProducer: KafkaProducerModule.Service[Any]
}

object KafkaProducerModule {
  trait Service[R] extends Serializable {
    def instance: fs2.kafka.KafkaProducer[Task, String, String]
  }
}
