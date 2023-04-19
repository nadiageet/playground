package com.example.playground.gift.batch;

import com.example.playground.gift.model.Gift;
import com.example.playground.gift.repository.GiftRepository;
import com.example.playground.quote.repository.UserRepository;
import com.example.playground.user.model.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.Map;

@Configuration
public class GiftBatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public GiftBatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job giftJob(Step giftStep) {
        return jobBuilderFactory.get("giftJob")
                .start(giftStep)
                .build();
    }

    @Bean
    ItemReader<User> userItemReader(UserRepository userRepository) {
        return new RepositoryItemReaderBuilder<User>()
                .name("readUsersFromDatabase")
                .repository(userRepository)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .methodName("findAll")
                .build();
    }

    @Bean
    ItemWriter<Gift> userItemWriter(GiftRepository giftRepository) {
        return new RepositoryItemWriterBuilder<Gift>()
                .repository(giftRepository)
                .build();
    }

    @Bean
    public Step giftStep(ItemReader<User> userItemReader,
                         GiveRandomQuoteGift giveRandomQuoteGift,
                         ItemWriter<Gift> userItemWriter) {
        return stepBuilderFactory.get("giftStep")
                .<User, Gift>chunk(100)
                .reader(userItemReader)
                .processor(giveRandomQuoteGift)
                .writer(userItemWriter)
                .build();
    }
}
