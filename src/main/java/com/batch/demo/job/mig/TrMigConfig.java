package com.batch.demo.job.mig;

import com.batch.demo.core.domain.account.Accounts;
import com.batch.demo.core.domain.account.AccountRepository;
import com.batch.demo.core.domain.orders.OrderRepository;
import com.batch.demo.core.domain.orders.Orders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;
import java.util.Collections;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TrMigConfig {

    /**
     * run : --job.name=trMigJob
     * desc : 주문 테이블 -> 정산 테이블 데이터 이관
     */

    private final OrderRepository orderRepository;

    private final AccountRepository accountRepository;

    @Bean
    public Job trMigJob(JobRepository jobRepository, Step trMigStep) {
        return new JobBuilder("trMigJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(trMigStep)
                .build();
    }

//  DB의 주문 데이터의 값 write 해보기
//    @JobScope
//    @Bean
//    public Step trMigStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, ItemReader trOrdersReader) {
//        return new StepBuilder("trMigStep", jobRepository)
//                .<Orders, Orders>chunk(5, platformTransactionManager) // <읽기,쓰기> 청크사이즈설정 => 읽을 데이터는 Orders 이고, 쓰는 데이터도 Orders이고 5개의 단위로 commit을 하겠다.
//                .reader(trOrdersReader)
//                .writer(new ItemWriter() {
//                    @Override
//                    public void write(Chunk chunk) throws Exception {
//                        for (Object orderInfo : chunk) {
//                            System.out.println("orderInfo = " + orderInfo.toString());
//                            /**
//                             * orderInfo = Orders(id=1, orderItem=카카오 선물, price=15000, orderDate=2022-03-01 00:00:00.0)
//                             * orderInfo = Orders(id=2, orderItem=배달주문, price=18000, orderDate=2022-03-01 00:00:00.0)
//                             * orderInfo = Orders(id=3, orderItem=교보문고, price=14000, orderDate=2022-03-02 00:00:00.0)
//                             * orderInfo = Orders(id=4, orderItem=아이스크림, price=3800, orderDate=2022-03-03 00:00:00.0)
//                             * orderInfo = Orders(id=5, orderItem=치킨, price=21000, orderDate=2022-03-04 00:00:00.0)
//                             * ---
//                             * orderInfo = Orders(id=6, orderItem=커피, price=4000, orderDate=2022-03-04 00:00:00.0)
//                             * orderInfo = Orders(id=7, orderItem=교보문고, price=13800, orderDate=2022-03-05 00:00:00.0)
//                             * orderInfo = Orders(id=8, orderItem=카카오 선물, price=5500, orderDate=2022-03-06 00:00:00.0)
//                             */
//                        }
//                    }
//                })
//                .build();
//    }

    @JobScope
    @Bean
    public Step trMigStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager,
                          ItemReader trOrdersReader, ItemProcessor trOrderProcessor, ItemWriter trOrdersWriter) {
        return new StepBuilder("trMigStep", jobRepository)
                .<Orders, Accounts>chunk(5, platformTransactionManager) // <읽기,쓰기> 청크사이즈설정 => 읽을 데이터는 Orders 이고, 쓰는 데이터도 Orders이고 5개의 단위로 commit을 하겠다.
                .reader(trOrdersReader)
                .processor(trOrderProcessor)
                .writer(trOrdersWriter)
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Orders, Accounts> trOrderProcessor() {
//        return new ItemProcessor<Orders, Account>() {
//            @Override
//            public Account process(Orders item) throws Exception {
//                return new Account(item); // 실무에서 작업을 할때는 좀 더 복잡한 로직이 만들어짐.
//            }
//        };
        return item -> {
            return new Accounts(item); // 실무에서 작업을 할때는 좀 더 복잡한 로직이 만들어짐.
        };
    }

    // 1. RepositoryItemWriter 사용하는 방법
//    @StepScope
//    @Bean
//    public RepositoryItemWriter<Accounts> trOrdersWriter() {
//        return new RepositoryItemWriterBuilder<Accounts>()
//                .repository(accountRepository)
//                .methodName("save")
//                .build();
//    }

    //2. RepositoryItemWriter 사용하지 않고 ItemWriter 직접 구현하는 방법
    @StepScope
    @Bean
    public ItemWriter<Accounts> trOrdersWriter() {
//        return new ItemWriter<Accounts>() {
//            @Override
//            public void write(Chunk<? extends Accounts> items) throws Exception {
//                items.forEach(item -> accountRepository.save(item));
//            }
//        };
        return items -> items.forEach(item -> accountRepository.save(item));
    }


    /**
     * Orders의 데이터 추출하기
     */
    @StepScope
    @Bean
    public RepositoryItemReader<Orders> trOrdersReader() {
        return new RepositoryItemReaderBuilder<Orders>()
                .name("trOrdersReader")
                .repository(orderRepository)
                .methodName("findAll")
                .pageSize(5) // 보통 chunkSize와 읽어 올 사이즈를 동일하게 한다.
                .arguments(Arrays.asList())
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC)) // 오름차순
                .build();
    }
}
