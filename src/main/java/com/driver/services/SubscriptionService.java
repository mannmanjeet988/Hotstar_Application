package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
          Subscription subscription = new Subscription();
       // subscriptionEntryDto.getUserId();
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());

        User user = userRepository.findById(subscriptionEntryDto.getUserId()).get();
        subscription.setUser(user);
        user.setSubscription(subscription);
        subscriptionRepository.save(subscription);
//        subscription.getStartSubscriptionDate();
//        subscription.getTotalAmountPaid();

       Integer amountPaid = 0;
        if(user.getSubscription().equals(SubscriptionType.BASIC)){
            amountPaid = 500 + (200 * subscription.getNoOfScreensSubscribed());
        } else if (user.getSubscription().equals(SubscriptionType.PRO))
        {
            amountPaid = 800 + (250 * subscription.getNoOfScreensSubscribed());
        } else {
            amountPaid = 1000 + (350 * subscription.getNoOfScreensSubscribed());
        }
        return amountPaid;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        User user = userRepository.findById(userId).get();
        Subscription subscription = user.getSubscription();
        Integer extraAmount= 0;
        if(user.getSubscription().equals(SubscriptionType.ELITE)){
            throw new Exception("Already the best Subscription");
        }
        if(user.getSubscription().equals(SubscriptionType.BASIC)){
            subscription.setSubscriptionType(SubscriptionType.PRO);
            userRepository.save(user);
            extraAmount= 300 + (50 * subscription.getNoOfScreensSubscribed());
        }
        else if(user.getSubscription().equals(SubscriptionType.PRO)){
            subscription.setSubscriptionType(SubscriptionType.ELITE);
            userRepository.save(user);
            extraAmount= 200 + (100 * subscription.getNoOfScreensSubscribed());
        }
        return extraAmount;
        //return null;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        List<User> userList = userRepository.findAll();
       // List<Subscription> subscriptionList = subs
        Integer revenue = 0;
           for(User user : userList){
               Subscription subscription = user.getSubscription();
               if(user.getSubscription().equals(SubscriptionType.BASIC)){
                   revenue += 500 + (200 * subscription.getNoOfScreensSubscribed());
               } else if (user.getSubscription().equals(SubscriptionType.PRO)){
                   revenue += 800 + (250 * subscription.getNoOfScreensSubscribed());
               } else {
                   revenue += 1000 + (350 * subscription.getNoOfScreensSubscribed());
               }
           }
           return revenue;
        //return null;
    }

}
