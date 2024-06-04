package com.batch.demo.job.file;

import com.batch.demo.job.file.dto.Player;
import com.batch.demo.job.file.dto.PlayerYears;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FileDataReadWriteConfig {
    @Bean
    public Job fileReadWriteJob(JobRepository jobRepository, Step fileReadWriteStep) {
        return new JobBuilder("fileReadWriteJob", jobRepository)
                .start(fileReadWriteStep)
                .build();
    }

    @JobScope
    @Bean
    public Step fileReadWriteStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, ItemReader playerItemReader,
                                  ItemProcessor playerItemProcessor, ItemWriter playerItemWriter) {
        return new StepBuilder("fileReadWriteStep", jobRepository)
                .<Player, PlayerYears>chunk(5, platformTransactionManager)
                .reader(playerItemReader)
                /**
                 * .<Player, Player>chunk(5, platformTransactionManager)의 값을 찍어서 확인해보기.
                 * .writer(items -> items.forEach(System.out::println))
                 * 데이터 확인
                 * Player(id=AbduKa00, lastName=Abdul-Jabbar, firstName=Karim, position=rb, birthYear=1974, debutYear=1996)
                 * Player(id=AbduRa00, lastName=Abdullah, firstName=Rabih, position=rb, birthYear=1975, debutYear=1999)
                 * Player(id=AberWa00, lastName=Abercrombie, firstName=Walter, position=rb, birthYear=1959, debutYear=1982)
                 * Player(id=AbraDa00, lastName=Abramowicz, firstName=Danny, position=wr, birthYear=1945, debutYear=1967)
                 * Player(id=AdamBo00, lastName=Adams, firstName=Bob, position=te, birthYear=1946, debutYear=1969)
                 * Player(id=AdamCh00, lastName=Adams, firstName=Charlie, position=wr, birthYear=1979, debutYear=2003)
                 */
                .processor(playerItemProcessor)
                .writer(playerItemWriter)
                .build();
    }

    // 읽어온 객체를 플레이어에 담는다.
    // 스프링 배치에서 제공하는 것을 사용해서 손쉽게 csv파일을 읽어서 저장할 수 있다.
    @StepScope
    @Bean
    public FlatFileItemReader<Player> playerItemReader() {
        return new FlatFileItemReaderBuilder<Player>()
                .name("playerItemReader")
                .resource(new FileSystemResource("Players.csv"))
                .lineTokenizer(new DelimitedLineTokenizer()) // 콤마 기준으로 나눈다.
                .fieldSetMapper(new PlayerFieldSetMapper())
                .linesToSkip(1) // 첫번째 줄은 스킵
                .build();
    }


    @StepScope
    @Bean
    // Player를 PlayerYears로 변경한다.
    public ItemProcessor<Player, PlayerYears> playerItemProcessor() {
        return new ItemProcessor<Player, PlayerYears>() {
            @Override
            public PlayerYears process(Player item) throws Exception {
                return new PlayerYears(item);
            }
        };
    }

    @StepScope
    @Bean
    public FlatFileItemWriter<PlayerYears> playerItemWriter() {
        BeanWrapperFieldExtractor<PlayerYears> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"id", "lastName", "position", "yearsExperience"});
        fieldExtractor.afterPropertiesSet();

        DelimitedLineAggregator lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);

        FileSystemResource outputResource = new FileSystemResource("players_output.txt");
        return new FlatFileItemWriterBuilder<PlayerYears>()
                .name("playerItemWriter")
                .resource(outputResource)
                .lineAggregator(lineAggregator)
                .build();
    }

}
